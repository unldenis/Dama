package vecchia;

public class Pedone {


  private final Colore colore;
  private boolean damone;
  private Cella cellaCorrente;

  public Pedone(Colore colore) {
    this.colore = colore;
    this.damone = false;
  }

  public Colore getColore() {
    return colore;
  }

  public boolean isDamone() {
    return damone;
  }

  public void setDamone(boolean damone) {
    this.damone = damone;
  }

  public Cella getCellaCorrente() {
    return cellaCorrente;
  }

  public void setCellaCorrente(Cella cellaCorrente) {
    this.cellaCorrente = cellaCorrente;
  }

  @Override
  public String toString() {
    return colore == Colore.NERO ? "\u25A0" : "\u25A1";
  }
}
