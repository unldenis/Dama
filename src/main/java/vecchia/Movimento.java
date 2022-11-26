package vecchia;

public enum Movimento {

  NORD_OVEST(-1, -1),
  NORD_EST(1, -1),
  SUD_OVEST(-1, 1),
  SUD_EST(1, 1),

  ;

  private final int movX;
  private final int movY;


  Movimento(int movX, int movY) {
    this.movX = movX;
    this.movY = movY;
  }

  public int getMovX() {
    return movX;
  }

  public int getMovY() {
    return movY;
  }
}
