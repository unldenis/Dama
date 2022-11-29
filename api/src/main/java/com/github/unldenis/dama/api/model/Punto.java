package com.github.unldenis.dama.api.model;

import java.io.Serializable;

/**
 * Rappresentazione di una coordinata 2D sulla damiera. Quando creato vengono calcolati anche i suoi
 * valori specchiati.
 */
public record Punto(int x, int y, int xSpec, int ySpec) implements Serializable {

  public Punto(int x, int y) {
    this(x, y, 7 - x, 7 - y);
  }
}
