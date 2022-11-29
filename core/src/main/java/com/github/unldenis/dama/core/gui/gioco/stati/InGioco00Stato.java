package com.github.unldenis.dama.core.gui.gioco.stati;

import com.github.unldenis.dama.api.model.Colore;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutAvversarioDisconnesso;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutPartitaTerminata;
import com.github.unldenis.dama.api.Constanti;
import com.github.unldenis.dama.core.gui.gioco.Gioco;
import com.github.unldenis.dama.core.net.client.Client;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Lo stato del client nel suo turno.
 */
public class InGioco00Stato extends Stato {

  private static final Logger logger = Logger.getLogger(InGioco00Stato.class.getName());

  public InGioco00Stato(Gioco gioco, Client client) {
    super(gioco, client);
    gioco.repaint();
  }

  @Override
  public void paint(Graphics2D g) {
    gioco.getGuiDamiera().disegna(g);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int x = e.getX() / Constanti.LATO_CELLA;
    int y = e.getY() / Constanti.LATO_CELLA;

    if (gioco.getColoreGiocatore() == Colore.NERO) {
      x = 7 - x;
      y = 7 - y;
    }

    var cella = gioco.getGuiDamiera().cellaInPosizione(x, y);
    var pedone = cella.getPedone();
    if (pedone != null && pedone.getColore() == gioco.getColoreGiocatore()) {
      gioco.setStatoCorrente(new InGioco01Stato(gioco, client, cella));
    }
  }

  @Override
  public void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
    if (tipo == TipoPacchetto.AVVERSARIO_DISCONNESSO) {
      var userNameAvversario = ((PacchettoOutAvversarioDisconnesso) pacchetto).getUsernameAvversario();
      logger.info("%s ha abbandonato la partita, hai vinto".formatted(userNameAvversario));
      client.disconnetti();
      gioco.setStatoCorrente(new TermineStato(gioco, client));
    } else if (tipo == TipoPacchetto.PARTITA_TERMINATA) {
      var p = (PacchettoOutPartitaTerminata) pacchetto;
      logger.info(
          "%s ha vinto la partita dopo %d turni.".formatted(p.getVincitore(), p.getTurni()));

      client.disconnetti();
      gioco.setStatoCorrente(new TermineStato(gioco, client));
    }
  }
}
