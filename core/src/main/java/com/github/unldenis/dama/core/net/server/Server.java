package com.github.unldenis.dama.core.net.server;

import com.github.unldenis.dama.api.model.Damiera;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutAvversarioDisconnesso;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutIniziaPartita;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutKeepAlive;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutPartitaTerminata;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che rappresenta un host(server). Un host può gestire solamente una partita in un
 * determinato istante.
 */
public abstract class Server extends Thread {

  private static final Logger logger = Logger.getLogger(Server.class.getName());

  private final int porta;
  private final ClientHandler[] giocatori = new ClientHandler[2];


  private Damiera damiera;
  private int turno = 0;
  private Timer timerKeepAlive;
  private int pronti = 0;

  public Server(int porta) {
    this.porta = porta;
  }

  @Override
  public void run() {
    ServerSocket server;
    try {
      server = new ServerSocket(porta);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore aprendo il socket", e);
      throw new RuntimeException(e);
    }

    logger.info("Aspettando i giocatori...");

    for (int i = 0; i < 2; i++) {
      aspettaGiocatore(server, i);
    }

    try {
      server.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore chiudendo il socket", e);
      throw new RuntimeException(e);
    }

    logger.info("Iniziando...");

    damiera = new Damiera();
    damiera.crea();

    // keep alive
    final var intervalloSecondi = 5;
    timerKeepAlive = new Timer();
    timerKeepAlive.schedule(new TimerTask() {
      @Override
      public void run() {
        for (var g : giocatori) {
          try {
            g.mandaPacchettoUnsafe(new PacchettoOutKeepAlive());
          } catch (IOException ignored) {
            break;
          }
        }
      }
    }, 1000L * intervalloSecondi, 1000L * intervalloSecondi);
  }

  public abstract void partitaFinita();

  protected void partitaTerminata() {
    // partita finita
    if (timerKeepAlive != null) {
      timerKeepAlive.cancel();
    }
    this.partitaFinita();
  }

  protected void partitaTerminata(int idVincitore) {
    final var nomeVincitore = giocatori[idVincitore].getUsername();
    broadcast(new PacchettoOutPartitaTerminata(nomeVincitore, turno));
    logger.info("%s ha vinto la partita dopo %d turni.".formatted(nomeVincitore, turno));

    for (var g : giocatori) {
      g.disconnetti();
    }

    this.partitaTerminata();
  }

  protected void connessionePersa(ClientHandler giocatore, String motivazione) {
    giocatore.disconnetti();

    // se il giocatore è 0 allora |0-1| = 1, se il giocatore è 1 allora |1-1| = 0
    var opponente = giocatori[Math.abs(giocatore.getId() - 1)];

    // opponente null quando la partita non è ancora iniziata
    if (opponente == null || !opponente.isConnesso()) {

      this.partitaTerminata();
    } else {
      logger.info("%s, %s ha vinto".formatted(motivazione, opponente.getUsername()));

      opponente.mandaPacchetto(new PacchettoOutAvversarioDisconnesso(giocatore.getUsername()));
    }

  }

  private Socket aspettaSocket(ServerSocket server) {
    try {
      return server.accept();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore I/O", e);
      throw new RuntimeException(e);
    }
  }

  private void aspettaGiocatore(ServerSocket server, int indice) {
    var socket = aspettaSocket(server);

    try {
      var clientHandler = new ClientHandler(this, indice, socket);
      giocatori[indice] = clientHandler;
      new Thread(clientHandler).start();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore inizializzando la connessione con il client", e);
      throw new RuntimeException(e);
    }
  }

  public void pronto() {
    pronti++;
    if (pronti == 2) {
      broadcast(new PacchettoOutIniziaPartita());
    }
  }

  public void broadcast(PacchettoOut pacchetto) {
    for (var g : giocatori) {
      g.mandaPacchetto(pacchetto);
    }
  }

  public void prossimoTurno() {
    this.turno++;
  }

  public Damiera getDamiera() {
    return damiera;
  }

  public int getTurno() {
    return turno;
  }
}
