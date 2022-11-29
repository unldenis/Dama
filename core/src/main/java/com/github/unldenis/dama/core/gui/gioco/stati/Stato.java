package com.github.unldenis.dama.core.gui.gioco.stati;

import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.core.gui.gioco.Gioco;
import com.github.unldenis.dama.core.net.client.Client;
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
