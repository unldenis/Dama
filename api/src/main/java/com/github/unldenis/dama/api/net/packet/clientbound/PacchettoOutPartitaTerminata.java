package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto che verrà inviato in broadcast quando un giocatore rimane senza pedoni.
 */
public class PacchettoOutPartitaTerminata extends PacchettoOut {

  private final String vincitore;
  private final int turni;

  public PacchettoOutPartitaTerminata(String vincitore, int turni) {
    super(TipoPacchetto.PARTITA_TERMINATA);
    this.vincitore = vincitore;
    this.turni = turni;
  }

  public String getVincitore() {
    return vincitore;
  }

  public int getTurni() {
    return turni;
  }
}
