package dama.gui.gioco.stati;

import dama.Constanti;
import dama.gui.gioco.Gioco;
import dama.model.Colore;
import dama.net.client.Client;
import dama.net.packet.TipoPacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import dama.net.packet.clientbound.PacchettoOutAvversarioDisconnesso;
import dama.net.packet.clientbound.PacchettoOutPartitaTerminata;
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

    var pedone = gioco.getGuiDamiera().cellaInPosizione(x, y).getPedone();
    if (pedone != null && pedone.getColore() == gioco.getColoreGiocatore()) {
      gioco.setStatoCorrente(new InGioco01Stato(gioco, client, pedone));
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
