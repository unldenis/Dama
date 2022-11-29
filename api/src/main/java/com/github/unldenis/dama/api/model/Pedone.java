package com.github.unldenis.dama.api.model;


/**
 * Un pedone(o nella sua forma finale, Damone). E' caratterizzato da un colore, e si può trovare in
 * una certa cella.
 */
public class Pedone {


  private final int id;
  private final Colore colore;
  private boolean damone;

  public Pedone(int id, Colore colore) {
    this.id = id;
    this.colore = colore;
    this.damone = false;
  }

  public Pedone clone() {
    var p = new Pedone(this.id, this.colore);
    p.damone = this.damone;
    return p;
  }

  public int getId() {
    return id;
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

  @Override
  public String toString() {
    return colore == Colore.NERO ? "\u25A0" : "\u25A1";
  }
}
