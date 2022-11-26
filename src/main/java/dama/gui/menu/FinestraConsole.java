package dama.gui.menu;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Finestra della console.
 */
public class FinestraConsole extends JFrame {


  public FinestraConsole() {
    super("Dama");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    setSize(677, 343);

    this.preparaPannelli();

//    pack();

    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void preparaPannelli() {
    JTextArea textArea = new JTextArea(50, 10);

    PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
    System.setOut(printStream);
    System.setErr(printStream);

    add(new JScrollPane(textArea));
  }


  private static class CustomOutputStream extends OutputStream {

    private final JTextArea textArea;

    private CustomOutputStream(JTextArea textArea) {
      this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
      // redirects data to the text area
      textArea.append(String.valueOf((char) b));
      // scrolls the text area to the end of data
      textArea.setCaretPosition(textArea.getDocument().getLength());
    }
  }
}
