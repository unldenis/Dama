package dama.model;

import java.io.Serializable;

/**
 * Tramite questa <a href="https://it.wikipedia.org/wiki/Dama#/media/File:Damiera.JPG">immagine</a>
 * di riferimento si possono distinguere i diversi movimenti dei pedoni. L'origine degli assi è in
 * alto a sinistra.
 */
public enum Movimento implements Serializable {

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

  public static Movimento ricercaMovimento(int deltaX, int deltaY) {
    for (var m : Movimento.values()) {
      if (m.getMovX() == deltaX && m.getMovY() == deltaY) {
        return m;
      }
    }
    return null;
  }

  public int getMovX() {
    return movX;
  }

  public int getMovY() {
    return movY;
  }
}
