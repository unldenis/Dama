package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto che viene trasmesso al giocatore rimasto in partita in caso di disconessione.
 */
public class PacchettoOutAvversarioDisconnesso extends PacchettoOut {

  private final String usernameAvversario;

  public PacchettoOutAvversarioDisconnesso(String usernameAvversario) {
    super(TipoPacchetto.AVVERSARIO_DISCONNESSO);
    this.usernameAvversario = usernameAvversario;
  }

  public String getUsernameAvversario() {
    return usernameAvversario;
  }
}
