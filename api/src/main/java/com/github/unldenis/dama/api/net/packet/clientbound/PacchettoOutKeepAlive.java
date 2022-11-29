package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Il server invierà frequentemente un keep-alive, per controllare lo stato dei giocatori in caso di
 * stallo.
 */
public class PacchettoOutKeepAlive extends PacchettoOut {

  public PacchettoOutKeepAlive() {
    super(TipoPacchetto.KEEP_ALIVE);
  }
}
