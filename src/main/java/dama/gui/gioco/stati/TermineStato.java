package dama.gui.gioco.stati;

import dama.gui.gioco.Gioco;
import dama.net.client.Client;
import dama.net.packet.TipoPacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Lo stato che rappresenta il termine della partita e rilascia la finestra di gioco.
 */
public class TermineStato extends Stato {

  public TermineStato(Gioco gioco, Client client) {
    super(gioco, client);

    gioco.getFinestraGioco().dispose();
  }

  @Override
  public void paint(Graphics2D g) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
  }
}
