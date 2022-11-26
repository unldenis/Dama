package dama.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Rappresentazione di un colore, usato per le celle e i pedoni.
 */
public enum Colore implements Serializable {
  BIANCO(Color.WHITE, Color.LIGHT_GRAY), //aliceblue
  NERO(new Color(205, 133, 63), Color.BLACK); // peru

  private final Color coloreAwt;
  private final Color colorePedone;

  Colore(Color coloreAwt, Color colorePedone) {
    this.coloreAwt = coloreAwt;
    this.colorePedone = colorePedone;
  }


  public Color coloreAwt() {
    return coloreAwt;
  }

  public Color colorePedone() {
    return colorePedone;
  }

}
