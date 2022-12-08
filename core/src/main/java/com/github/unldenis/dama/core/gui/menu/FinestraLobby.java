package com.github.unldenis.dama.core.gui.menu;

import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.github.unldenis.dama.api.Constanti;
import com.github.unldenis.dama.api.gui.FinestraConsole;
import com.github.unldenis.dama.api.gui.GuiDamiera;
import com.github.unldenis.dama.core.gui.gioco.FinestraGioco;
import com.github.unldenis.dama.core.net.server.Server;
import com.github.unldenis.dama.core.uti.Utilita;
import com.github.unldenis.dama.solo.gui.PannelloDifficolta;
import java.awt.EventQueue;
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
import javax.swing.JSplitPane;
import javax.swing.JTextField;

/**
 * Finestra principale con la quale si possono hostare delle partite o giocare.
 */
public class FinestraLobby extends JFrame {


  public FinestraLobby() {
    super("Dama");

//    setUndecorated(true);
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setIconImage(GuiDamiera.damoneNeroImg);
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

    EventQueue.invokeLater(()-> {
      FlatMonokaiProIJTheme.setup();
      new FinestraLobby();
    });
  }

  private void preparaMenuBar() {
    var mb = new JMenuBar();

    // hosta
    var y = new JMenu("Hosta");

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

    var mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gioca(), new PannelloDifficolta(this));
    mainPane.setEnabled(false);
    add(mainPane);
  }

  private JPanel gioca() {
    JPanel panel = new JPanel(new GridBagLayout());

    GridBagConstraints constr = new GridBagConstraints();
    constr.insets = new Insets(5, 5, 5, 5);
    constr.anchor = GridBagConstraints.WEST;


    constr.gridx = 0;
    constr.gridy = 0;

    // Labels
    JLabel info = new JLabel("Gioca multiplayer");
    JLabel userNameLabel = new JLabel("Inserisci il tuo nome :");
    JLabel ipLabel = new JLabel("Inserisci l'indirizzo ip :");

    // Text fields
    JTextField userNameTxt = new JTextField(20);

    JTextField ipTxt = new JTextField(20);
    ipTxt.setText("localhost");

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
