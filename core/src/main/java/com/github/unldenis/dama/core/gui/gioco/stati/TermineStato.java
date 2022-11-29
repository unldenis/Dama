package com.github.unldenis.dama.core.gui.gioco.stati;

import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.core.gui.gioco.Gioco;
import com.github.unldenis.dama.core.net.client.Client;
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
