package com.github.unldenis.dama.core.gui.gioco.stati;

import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutAvversarioDisconnesso;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutMuovi;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOutPartitaTerminata;
import com.github.unldenis.dama.core.gui.gioco.Gioco;
import com.github.unldenis.dama.core.net.client.Client;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Lo stato del client ma nel turno dell'avversario. Non puo' interagire col campo, tuttavia nel
 * movimento di un pedone avversario lo stato ritorna a InGioco00.
 */
public class InGioco02Stato extends Stato {

  private static final Logger logger = Logger.getLogger(InGioco02Stato.class.getName());

  public InGioco02Stato(Gioco gioco, Client client) {
    super(gioco, client);
    gioco.repaint();
  }

  @Override
  public void paint(Graphics2D g) {
    gioco.getGuiDamiera().disegna(g);
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
    switch (tipo) {
      case AVVERSARIO_DISCONNESSO -> {
        var userNameAvversario = ((PacchettoOutAvversarioDisconnesso) pacchetto).getUsernameAvversario();
        logger.info("%s ha abbandonato la partita, hai vinto".formatted(userNameAvversario));
        client.disconnetti();
        gioco.setStatoCorrente(new TermineStato(gioco, client));
      }
      case PARTITA_TERMINATA -> {
        var p = (PacchettoOutPartitaTerminata) pacchetto;
        logger.info(
            "%s ha vinto la partita dopo %d turni.".formatted(p.getVincitore(), p.getTurni()));

        client.disconnetti();
        gioco.setStatoCorrente(new TermineStato(gioco, client));
      }
      case MUOVI -> {
        var damiera = gioco.getGuiDamiera();

        var p = (PacchettoOutMuovi) pacchetto;

        var cella = damiera.cellaInPosizione(p.getCellaPedone().x(), p.getCellaPedone().y());
        var movimento = p.getMovimento();
        var codice = p.getResultMovimento();

        if (codice.movimentoAutorizzato()) {
//          System.out.printf("Muovendo %s a %s%n", pedone.toString(), movimento.name());
          damiera.muovi(cella, movimento, false);

          gioco.setStatoCorrente(new InGioco00Stato(gioco, client));
        }
      }
    }
  }
}
