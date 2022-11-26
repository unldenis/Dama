package dama.net.client;

import dama.net.packet.Pacchetto;
import dama.net.packet.TipoPacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import dama.net.packet.serverbound.PacchettoIn;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che permette la comunicazione con il server.
 */
public class Client extends Thread {

  private static final Logger logger = Logger.getLogger(Client.class.getName());

  private final String indirizzoServer;
  private final int porta;
  private final Object alServerLock = new Object();
  private final AtomicBoolean connesso = new AtomicBoolean(true);
  private Socket socket;
  private ObjectOutputStream alServer;

  public Client(String indirizzoServer, int porta) {
    this.indirizzoServer = indirizzoServer;
    this.porta = porta;
  }

  public void connetti() {
    try {
      socket = new Socket(indirizzoServer, porta);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore I/O", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    ObjectInputStream dalServer;
    try {
      alServer = new ObjectOutputStream(socket.getOutputStream());
      dalServer = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore inizializzando la connessione con il server", e);
      throw new RuntimeException(e);
    }

    synchronized (alServerLock) {
      alServerLock.notify();
    }

    PacchettoOut pacchetto;
    try {
      while (isConnesso() && (pacchetto = (PacchettoOut) dalServer.readObject()) != null) {

        gestisciPacchetto(pacchetto, Pacchetto.ricercaPacchetto(pacchetto.packetId()));
      }

      alServer.close();
      dalServer.close();
      socket.close();

    } catch (IOException | ClassNotFoundException e) {
//      e.printStackTrace();
      serverDisconnesso();
    }
  }


  protected void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
  }

  protected void serverDisconnesso() {
  }

  public void mandaPacchetto(PacchettoIn pacchetto) {
    if (alServer == null) {
      synchronized (alServerLock) {
        try {
          alServerLock.wait();
        } catch (InterruptedException e) {
          logger.log(Level.SEVERE, "Errore wait()", e);
          throw new RuntimeException(e);
        }
      }
    }

    try {
      alServer.writeObject(pacchetto);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Errore invio del pacchetto " + pacchetto, e);
      throw new RuntimeException(e);
    }
  }

  public boolean isConnesso() {
    return connesso.get();
  }

  public void disconnetti() {
    connesso.set(false);
  }
}
