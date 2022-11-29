package com.github.unldenis.dama.api.net.packet.serverbound;

import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto che permette ad un giocatore di richiedere il suo ID e inviare a sua volta il suo
 * username.
 */
public class PacchettoInIdGiocatore extends PacchettoIn {

  private final String username;

  public PacchettoInIdGiocatore(String username) {
    super(TipoPacchetto.ID_GIOCATORE);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
