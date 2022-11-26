package dama.gui.menu;

import dama.Constanti;
import dama.gui.gioco.FinestraGioco;
import dama.net.server.Server;
import dama.uti.Utilita;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Finestra principale con la quale si possono hostare delle partite o giocare.
 */
public class FinestraLobby extends JFrame {

  public static final Color aC = Color.decode("#252323");
  public static final Color cC = Color.decode("#f5f1ed");
  public static final Color dC = Color.decode("#dad2bc");
  public static final Color eC = Color.decode("#a99985");

  public FinestraLobby() {
    super("Dama");

//    setUndecorated(true);
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    setSize(677, 343);


    this.preparaMenuBar();
    this.preparaPannelli();

//    this.pack();

    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(String[] args) {
//    JFrame.setDefaultLookAndFeelDecorated(true);
    new FinestraLobby();
  }

  private void preparaMenuBar() {
    var mb = new JMenuBar();

    // hosta
    var y = new JMenu("Hosta");
    y.setOpaque(true);
    y.setBackground(eC);
    y.setForeground(cC);

    var y0 = new JMenuItem("Avvia");

    y0.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        dispose();
        new FinestraConsole();
        new Server(Constanti.PORTA_TCP) {
          @Override
          public void partitaFinita() {
          }
        }.start();
      }
    });

    // help
    var x = new JMenu("Aiuto");
    x.setOpaque(true);
    x.setBackground(eC);
    x.setForeground(cC);

    // create menuitems
    var m0 = new JMenuItem("Codice");

    var m1 = new JMenuItem("About");

    final String sj = """
        Dama %s
                
        Denis Mehilli & Marco Rech
                
        5IE 2022/2023
        ITI Severi
        """.formatted(Constanti.VERSIONE);

    m0.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Utilita.openWebpage("https://github.com/unldenis/Dama");
      }
    });

    m1.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        JOptionPane.showMessageDialog(FinestraLobby.this, sj, "About Dama",
            JOptionPane.PLAIN_MESSAGE);
      }
    });

    // add menu items to menu
    y.add(y0);

    x.add(m0);
    x.add(m1);

    // add menu to menu bar
    mb.add(y);
    mb.add(x);

    // add menubar to frame
    setJMenuBar(mb);
  }

  private void preparaPannelli() {
    add(gioca());
  }

  private JPanel gioca() {
    JPanel panel = new JPanel(new GridBagLayout());

    GridBagConstraints constr = new GridBagConstraints();
    constr.insets = new Insets(5, 5, 5, 5);
    constr.anchor = GridBagConstraints.WEST;

    constr.gridx = 0;
    constr.gridy = 0;

    // Labels
    JLabel info = new JLabel("Entra in una partita");
    info.setForeground(aC);

    JLabel userNameLabel = new JLabel("Inserisci il tuo nome :");
    userNameLabel.setForeground(eC);

    JLabel ipLabel = new JLabel("Inserisci l'ip :");
    ipLabel.setForeground(eC);

    // Text fields
    JTextField userNameTxt = new JTextField(20);
    userNameTxt.setForeground(aC);

    JTextField ipTxt = new JTextField(20);
    ipTxt.setText("localhost");
    ipTxt.setForeground(aC);

    constr.gridx = 1;
    panel.add(info, constr);
    constr.gridx = 0;
    constr.gridy = 1;

    panel.add(userNameLabel, constr);
    constr.gridx = 1;
    panel.add(userNameTxt, constr);
    constr.gridx = 0;
    constr.gridy = 2;

    panel.add(ipLabel, constr);
    constr.gridx = 1;
    panel.add(ipTxt, constr);
    constr.gridx = 1;
    constr.gridy = 3;

    constr.gridwidth = 2;
    constr.anchor = GridBagConstraints.CENTER;

    JButton button = new JButton("Gioca");
    button.setBackground(dC);
    button.setForeground(eC);
    button.setFocusPainted(false);
    button.addActionListener(event -> {
      dispose();
      new FinestraConsole();
      new FinestraGioco(ipTxt.getText(), Constanti.PORTA_TCP, userNameTxt.getText());
      userNameTxt.setText("");

    });

    panel.add(button, constr);

    return panel;

  }

  private String calcolaIndirizzi() {
    try {
      return InetAddress.getLocalHost().getHostAddress() + " \\ " + Utilita.indirizzo_pubblico();
    } catch (IOException e) {
      return "Na";
    }
  }
}
