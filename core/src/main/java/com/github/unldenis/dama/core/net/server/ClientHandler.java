package com.github.unldenis.dama.core.net.server;

import com.github.unldenis.dama.api.model.Movimento;
import com.github.unldenis.dama.api.model.Punto;
import com.github.unldenis.dama.api.model.ResultMovimento;
import com.github.unldenis.dama.api.model.ResultMovimento.OkMovimento;
import com.github.unldenis.dama.api.net.packet.Pacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutIdGiocatore;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutMuovi;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutPossibiliMovimenti;
import com.github.unldenis.dama.api.net.packet.serverbound.PacchettoIn;
import com.github.unldenis.dama.api.net.packet.serverbound.PacchettoInIdGiocatore;
import com.github.unldenis.dama.api.net.packet.serverbound.PacchettoInMuovi;
import com.github.unldenis.dama.api.net.packet.serverbound.PacchettoInPossibiliMovimenti;
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

            var movimentiDisponibili = Arrays.stream(Movimento.values()).filter(movimento -> {
              var s = damiera.muovi(cella, movimento, true);
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

            var codice = damiera.muovi(cella, movimento, false);
            OptionalInt vincitore = OptionalInt.empty();

            if (codice.movimentoAutorizzato() && codice instanceof OkMovimento okMovimento) {
              if (okMovimento.mangia()) {

                if (damiera.getPedoniNeri() == 0) {
                  vincitore = OptionalInt.of(0);
                } else if (damiera.getPedoniBianchi() == 0) {
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
      if (damiera.getPedoniBianchi() > 0 && damiera.getPedoniNeri() > 0) {
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
