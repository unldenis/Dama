package vecchia;

import java.util.Scanner;

public class DamaLocale {

  private static final Scanner input = new Scanner(System.in);

  public static void main(String[] args) {
    String giocatore1, giocatore2;

    giocatore1 = leggi("Inserisci nome giocatore 1: ");
    giocatore2 = leggi("Inserisci nome giocatore 2: ");

    var damiera = new Damiera();
    damiera.crea();


  }

  private static String leggi(String m) {
    System.out.println(m);
    System.out.print("> ");
    return input.nextLine();
  }

}
