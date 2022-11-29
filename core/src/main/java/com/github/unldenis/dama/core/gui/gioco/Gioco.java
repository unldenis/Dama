package com.github.unldenis.dama.core.gui.gioco;

import com.github.unldenis.dama.api.gui.GuiDamiera;
import com.github.unldenis.dama.api.model.Colore;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import com.github.unldenis.dama.api.net.packet.clientbound.PacchettoOut;
import com.github.unldenis.dama.api.net.packet.serverbound.PacchettoInIdGiocatore;
import com.github.unldenis.dama.api.Constanti;
import com.github.unldenis.dama.core.gui.gioco.stati.LobbyStato;
import com.github.unldenis.dama.core.gui.gioco.stati.Stato;
import com.github.unldenis.dama.core.gui.gioco.stati.TermineStato;
import com.github.unldenis.dama.core.net.client.Client;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Classe che gestisce la partita nel client, contiene lo stato corrente, la damiera e il client per
 * la comunicazione al server.
 */
public class Gioco extends JPanel implements MouseListener {

  private static final Logger logger = Logger.getLogger(Gioco.class.getName());

  private final FinestraGioco finestraGioco;
  private final Client client;

  private Stato statoCorrente;


  private Colore coloreGiocatore;
  private GuiDamiera guiDamiera;


  public Gioco(FinestraGioco finestraGioco, String ip, int porta) {
    this.finestraGioco = finestraGioco;
    setPreferredSize(new Dimension(Constanti.LATO_FINESTRA, Constanti.LATO_FINESTRA));
    setDoubleBuffered(true);

    addMouseListener(this);
    setFocusable(true);

    client = new Client(ip, porta) {
      @Override
      protected void gestisciPacchetto(PacchettoOut pacchetto, TipoPacchetto tipo) {
        switch (tipo) {
          case INVALIDO, KEEP_ALIVE -> {

          }
          default -> statoCorrente.gestisciPacchetto(pacchetto, tipo);
        }
      }

      @Override
      protected void serverDisconnesso() {
        statoCorrente = new TermineStato(Gioco.this, this);
      }
    };
    statoCorrente = new LobbyStato(this, client);
  }

  public void connect(String username) {
    // start connection
    client.connetti();
    client.start();
    client.mandaPacchetto(new PacchettoInIdGiocatore(username));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    var g2 = (Graphics2D) g;
    statoCorrente.paint(g2);
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    statoCorrente.mousePressed(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  public FinestraGioco getFinestraGioco() {
    return finestraGioco;
  }

  public Client getClient() {
    return client;
  }

  public Stato getStatoCorrente() {
    return statoCorrente;
  }

  public void setStatoCorrente(Stato statoCorrente) {
    this.statoCorrente = statoCorrente;
  }

  public Colore getColoreGiocatore() {
    return coloreGiocatore;
  }

  public void setColoreGiocatore(Colore coloreGiocatore) {
    this.coloreGiocatore = coloreGiocatore;
  }

  public GuiDamiera getGuiDamiera() {
    return guiDamiera;
  }

  public void setGuiDamiera(GuiDamiera guiDamiera) {
    this.guiDamiera = guiDamiera;
  }
}
