package dama.gui.gioco;

import dama.gui.gioco.stati.TermineStato;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * Finestra del gioco.
 */
public class FinestraGioco extends JFrame {

  private static final Logger logger = Logger.getLogger(FinestraGioco.class.getName());

  public FinestraGioco(String ip, int porta, String username) {
    super("Dama");

//    setUndecorated(true);
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    Gioco panello = new Gioco(this, ip, porta);
    add(panello);

    EventQueue.invokeLater(() -> {
      try {
        panello.connect(username);
      } catch (RuntimeException e) {
        dispose();
      }
    });

    pack();

    setLocationRelativeTo(null);
    setVisible(true);

    /*Some piece of code*/
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        logger.info("Hai abbandonato la partita");
        var client = panello.getClient();
        client.disconnetti();
        panello.setStatoCorrente(new TermineStato(panello, client));
      }
    });
  }
}
