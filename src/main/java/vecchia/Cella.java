package vecchia;

public class Cella {

  private final int x, y;
  private final Colore colore;

  private Pedone pedone;

  public Cella(int x, int y, Colore colore) {
    this.x = x;
    this.y = y;
    this.colore = colore;
  }


  public int getX() {
    return x;
  }

  public int getY() {
    return y;
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
