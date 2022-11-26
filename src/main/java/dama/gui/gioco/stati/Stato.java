package dama.gui.gioco.stati;

import dama.gui.gioco.Gioco;
import dama.net.client.Client;
import dama.net.packet.TipoPacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Classe che rappresenta uno stato per i giocatori.
 */
public abstract class Stato {

  protected final Gioco gioco;
  protected final Client client;

  public Stato(Gioco gioco, Client client) {
    this.gioco = gioco;
    this.client = client;
  }

  public abstract void paint(Graphics2D g);

  public abstract void mousePressed(MouseEvent e);

  public abstract void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo);


}
