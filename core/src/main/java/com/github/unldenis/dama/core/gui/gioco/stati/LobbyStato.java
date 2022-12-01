package com.github.unldenis.dama.core.gui.gioco.stati;

import com.github.unldenis.dama.api.Constanti;
import com.github.unldenis.dama.api.gui.GuiDamiera;
import com.github.unldenis.dama.api.model.Colore;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutIdGiocatore;
import com.github.unldenis.dama.core.gui.gioco.Gioco;
import com.github.unldenis.dama.core.net.client.Client;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Lo stato iniziale quando ci connettiamo al server, riceve l'ID del giocatore e ascolta quando la
 * partita inizia.
 */
public class LobbyStato extends Stato {

  private static final Logger logger = Logger.getLogger(LobbyStato.class.getName());

  public LobbyStato(Gioco gioco, Client client) {
    super(gioco, client);
  }

  @Override
  public void paint(Graphics2D g) {
    var colore = gioco.getColoreGiocatore();
    if (colore != null) {
      g.setColor(Color.BLACK);
      g.drawString("Aspettando il tuo avversario...", 50, Constanti.LATO_FINESTRA / 2);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
    switch (tipo) {
      case ID_GIOCATORE -> {
        var p = ((PacchettoOutIdGiocatore) pacchetto).idGiocatore();
        gioco.setColoreGiocatore(p == 0 ? Colore.BIANCO : Colore.NERO);
        logger.info("ID Giocatore " + p);
        gioco.repaint();
      }
      case INIZIA_PARTITA -> {
        var guiDamiera = new GuiDamiera(gioco.getColoreGiocatore());
        guiDamiera.crea();
        gioco.setGuiDamiera(guiDamiera);

        logger.info("Partita iniziata");

        gioco.setStatoCorrente(
            gioco.getColoreGiocatore() == Colore.NERO ? new InGioco00Stato(gioco, client)
                : new InGioco02Stato(gioco, client));
      }
    }
  }
}
