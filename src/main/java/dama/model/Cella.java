package dama.model;

/**
 * Rappresentazione di una cella della damiera. Ciascuna cella ha una coordinata, un colore, ed
 * eventualmente un pedone.
 */
public class Cella {

  private final Punto coordinata;
  private final Colore colore;

  private Pedone pedone;

  public Cella(Punto coordinata, Colore colore) {
    this.coordinata = coordinata;
    this.colore = colore;
  }

  public String formatId() {
    return "%d:%d".formatted(getY(), getX());
  }

  public int getX() {
    return coordinata.x();
  }

  public int getY() {
    return coordinata.y();
  }

  public Punto getCoordinata() {
    return coordinata;
  }

  public Colore getColore() {
    return colore;
  }

  public Pedone getPedone() {
    return pedone;
  }

  public void setPedone(Pedone pedone) {
    this.pedone = pedone;
  }

  @Override
  public String toString() {
    return pedone == null ? "-" : pedone.toString();
  }
}
