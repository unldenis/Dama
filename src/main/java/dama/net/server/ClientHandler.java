package dama.net.server;

import dama.Constanti;
import dama.model.Colore;
import dama.model.Movimento;
import dama.model.Punto;
import dama.model.ResultMovimento;
import dama.model.ResultMovimento.OkMovimento;
import dama.net.packet.Pacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import dama.net.packet.clientbound.PacchettoOutIdGiocatore;
import dama.net.packet.clientbound.PacchettoOutMuovi;
import dama.net.packet.clientbound.PacchettoOutPossibiliMovimenti;
import dama.net.packet.serverbound.PacchettoIn;
import dama.net.packet.serverbound.PacchettoInIdGiocatore;
import dama.net.packet.serverbound.PacchettoInMuovi;
import dama.net.packet.serverbound.PacchettoInPossibiliMovimenti;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che serve a gestire i diversi giocatori.
 */
public class ClientHandler implements Runnable {

  private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

  private final Server server;
  private final int id;
  private final Socket socket;
  private final ObjectOutputStream alClient;
  private final ObjectInputStream dalClient;

  private final AtomicBoolean connesso = new AtomicBoolean(true);


  private String username;

  public ClientHandler(Server server, int id, Socket socket) throws IOException {
    this.server = server;
    this.id = id;
    this.socket = socket;
    this.alClient = new ObjectOutputStream(socket.getOutputStream());
    this.dalClient = new ObjectInputStream(socket.getInputStream());
  }

  @Override
  public void run() {

    PacchettoIn pacchetto;
    try {
      while (connesso.get() && (pacchetto = (PacchettoIn) dalClient.readObject()) != null) {

        switch (Pacchetto.ricercaPacchetto(pacchetto.packetId())) {
          case INVALIDO -> {

          }
          case ID_GIOCATORE -> {
            username = ((PacchettoInIdGiocatore) pacchetto).getUsername();
            logger.info("Giocatore %d(%s) e' entrato".formatted(id, username));
            mandaPacchetto(new PacchettoOutIdGiocatore(id));

            server.pronto();
          }

          case POSSIBILI_MOVIMENTI -> {
            var damiera = server.getDamiera();

            var punto = ((PacchettoInPossibiliMovimenti) pacchetto).getCellaPedone();
            var cella = damiera.cellaInPosizione(punto.x(), punto.y());

            var pedone = cella.getPedone();
            var movimentiDisponibili = Arrays.stream(Movimento.values()).filter(movimento -> {
              var s = damiera.puoMuoversi(pedone, movimento, true);
              return s.movimentoAutorizzato();
            }).map(movimento -> new Punto(cella.getX() + movimento.getMovX(),
                cella.getY() + movimento.getMovY())).toList();
            mandaPacchetto(new PacchettoOutPossibiliMovimenti(movimentiDisponibili));
          }

          case MUOVI -> {
            var damiera = server.getDamiera();
            var p = (PacchettoInMuovi) pacchetto;
            var movimento = p.getMovimento();

            // controlla il turno
            if (server.getTurno() % 2 == id) {
              mandaPacchetto(
                  new PacchettoOutMuovi(p.getCellaPedone(), movimento,
                      ResultMovimento.err("Aspetta il tuo turno")));
              return;
            }

            var cella = damiera.cellaInPosizione(p.getCellaPedone().x(), p.getCellaPedone().y());
            var pedone = cella.getPedone();

            var codice = damiera.puoMuoversi(pedone, movimento, false);
            OptionalInt vincitore = OptionalInt.empty();

            if (codice.movimentoAutorizzato() && codice instanceof OkMovimento okMovimento) {
              if (okMovimento.mangia()) {
//                damiera.muovi(pedone, movimento);

                if (pedone.getColore() == Colore.BIANCO) {
                  damiera.pedoniNeri--;
                } else {
                  damiera.pedoniBianchi--;
                }

                if (damiera.pedoniNeri == 0) {
                  vincitore = OptionalInt.of(0);
                } else if (damiera.pedoniBianchi == 0) {
                  vincitore = OptionalInt.of(1);
                }
              }
              server.prossimoTurno();
            }

            server.broadcast(new PacchettoOutMuovi(p.getCellaPedone(), movimento, codice));
            vincitore.ifPresent(server::partitaTerminata);

          }
        }
      }
      disconnetti();
    } catch (IOException | ClassNotFoundException e) {
      var damiera = server.getDamiera();
      if (damiera.pedoniBianchi > 0 && damiera.pedoniNeri > 0) {
        server.connessionePersa(this, username + " ha abbandonato la partita");
      }
    }
  }

  public void disconnetti() {
    connesso.set(false);
    try {
      alClient.close();
      dalClient.close();
      socket.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore nella chiusura della connessione", e);
      throw new RuntimeException(e);
    }

  }

  public void mandaPacchetto(PacchettoOut pacchetto) {
    try {
      mandaPacchettoUnsafe(pacchetto);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore nell'invio del pacchetto " + pacchetto, e);
      throw new RuntimeException(e);
    }
  }

  public void mandaPacchettoUnsafe(PacchettoOut pacchetto) throws IOException {
    alClient.writeObject(pacchetto);
  }


  public int getId() {
    return id;
  }

  public boolean isConnesso() {
    return connesso.get();
  }

  public String getUsername() {
    return username;
  }
}
