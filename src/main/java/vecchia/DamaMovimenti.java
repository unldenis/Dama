package vecchia;

public class DamaMovimenti {

  public static void main(String[] args) {
    var damiera = new Damiera();
    damiera.crea();

    var pedone = damiera.cellaInPosizione(2, 5).getPedone();

    damiera.cellaInPosizione(6, 1).setPedone(null);
    damiera.pedoniNeri--;

    damiera.cellaInPosizione(7, 0).setPedone(null);
    damiera.pedoniNeri--;

    damiera.stampa();

    muovi(damiera, pedone);
    muovi(damiera, pedone);
    muovi(damiera, pedone);
    muovi(damiera, pedone);

    muovi(damiera, damiera.cellaInPosizione(7, 6).getPedone());
  }

  private static void muovi(Damiera damiera, Pedone pedone) {
    var codice = damiera.puoMuoversi(pedone, Movimento.NORD_EST);

    if (codice.equals("mangia")) {
      damiera.muovi(pedone, Movimento.NORD_EST);

      if (pedone.getColore() == Colore.BIANCO) {
        damiera.pedoniNeri--;
      } else {
        damiera.pedoniBianchi--;
      }

      if (damiera.pedoniNeri == 0 || damiera.pedoniBianchi == 0) {
        // finito
        System.exit(0);
      }
    } else if (codice.equals("")) {
      damiera.muovi(pedone, Movimento.NORD_EST);
    }

    System.out.println();
    damiera.stampa();
    System.out.println("Codice " + codice);
    System.out.println("Pedoni neri correnti " + damiera.pedoniNeri);
    System.out.println("Pedoni bianchi correnti " + damiera.pedoniBianchi);
  }
}
