package dama.gui.gioco.stati;

import dama.Constanti;
import dama.gui.gioco.Gioco;
import dama.model.Colore;
import dama.model.Movimento;
import dama.model.Pedone;
import dama.model.Punto;
import dama.model.ResultMovimento.ErrMovimento;
import dama.net.client.Client;
import dama.net.packet.TipoPacchetto;
import dama.net.packet.clientbound.PacchettoOut;
import dama.net.packet.clientbound.PacchettoOutAvversarioDisconnesso;
import dama.net.packet.clientbound.PacchettoOutMuovi;
import dama.net.packet.clientbound.PacchettoOutPartitaTerminata;
import dama.net.packet.clientbound.PacchettoOutPossibiliMovimenti;
import dama.net.packet.serverbound.PacchettoInMuovi;
import dama.net.packet.serverbound.PacchettoInPossibiliMovimenti;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Lo stato del client sempre nel suo turno, ma dopo aver cliccato un suo pedone. Evidenza la cella
 * selezionata oltre ai possibili movimenti di un pedone. I possibili movimenti vengono calcolati
 * nel server. Viene richiesto un movimento: se nella stessa cella si torna allo stato InGioco00;
 * oppure richiede un movimento al server.
 */
public class InGioco01Stato extends Stato {

  private static final Logger logger = Logger.getLogger(InGioco01Stato.class.getName());

  private final Pedone pedoneSelezionato;

  private List<Punto> possibiliMovimenti;

  public InGioco01Stato(Gioco gioco, Client client, Pedone pedoneSelezionato) {
    super(gioco, client);
    this.pedoneSelezionato = pedoneSelezionato;

    client.mandaPacchetto(new PacchettoInPossibiliMovimenti(pedoneSelezionato.getCellaCorrente()
        .getCoordinata()));
    gioco.repaint();
  }

  @Override
  public void paint(Graphics2D g) {
    var guiDamiera = gioco.getGuiDamiera();
    guiDamiera.disegnaEvidenzaCella(g, pedoneSelezionato.getCellaCorrente(), possibiliMovimenti);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int x = e.getX() / Constanti.LATO_CELLA;
    int y = e.getY() / Constanti.LATO_CELLA;

    if (gioco.getColoreGiocatore() == Colore.NERO) {
      x = 7 - x;
      y = 7 - y;
    }

    var cellaPart = pedoneSelezionato.getCellaCorrente();
    var cellaDest = gioco.getGuiDamiera().cellaInPosizione(x, y);

    var pedone = cellaDest.getPedone();
    if (pedone != null && pedone == pedoneSelezionato) {
      gioco.setStatoCorrente(new InGioco00Stato(gioco, client));
    } else {

      var movimento = Movimento.ricercaMovimento(cellaDest.getX() - cellaPart.getX(),
          cellaDest.getY() - cellaPart.getY());
      if (movimento != null) {
        client.mandaPacchetto(new PacchettoInMuovi(cellaPart.getCoordinata(), movimento));
      } else {
        logger.warning("Movimento sconosciuto...");
      }
    }

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
      case POSSIBILI_MOVIMENTI -> {
        possibiliMovimenti = ((PacchettoOutPossibiliMovimenti) pacchetto).getCoordinate();
        gioco.repaint();
      }
      case MUOVI -> {
        var damiera = gioco.getGuiDamiera();

        var p = (PacchettoOutMuovi) pacchetto;

        var cella = damiera.cellaInPosizione(p.getCellaPedone().x(), p.getCellaPedone().y());
        var pedone = cella.getPedone();
        var movimento = p.getMovimento();
        var codice = p.getResultMovimento();

        if (codice.movimentoAutorizzato()) {
//          System.out.printf("Muovendo %s a %s%n", pedone.toString(), movimento.name());
          damiera.puoMuoversi(pedone, movimento, false);

          gioco.setStatoCorrente(new InGioco02Stato(gioco, client));

        } else {
          if (pedone.getColore() == gioco.getColoreGiocatore()) {
            JOptionPane.showMessageDialog(gioco, ((ErrMovimento) codice).errore(), "Movimento non valido",
                JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    }
  }
}
