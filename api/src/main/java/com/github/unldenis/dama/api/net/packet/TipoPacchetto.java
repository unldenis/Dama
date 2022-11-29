package com.github.unldenis.dama.api.net.packet;

/**
 * Enumerativo che rappresenta i vari pacchetti.
 */
public enum TipoPacchetto {

  INVALIDO(0x00),

  ID_GIOCATORE(0x01),

  INIZIA_PARTITA(0x02),

  MUOVI(0x03),

  POSSIBILI_MOVIMENTI(0x04),

  AVVERSARIO_DISCONNESSO(0x05),

  KEEP_ALIVE(0x06),

  PARTITA_TERMINATA(0X07);

  private final int id;

  TipoPacchetto(int id) {
    this.id = id;
  }

  public int id() {
    return id;
  }
}
