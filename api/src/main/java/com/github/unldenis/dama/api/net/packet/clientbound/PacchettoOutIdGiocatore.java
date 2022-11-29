package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto risposta al giocatore con il suo ID. Al momento un ID pari a 0 vuol dire far parte
 * della 'squadra' bianca, altrimenti nera.
 */
public class PacchettoOutIdGiocatore extends PacchettoOut {

  private final int idGiocatore;

  public PacchettoOutIdGiocatore(int idGiocatore) {
    super(TipoPacchetto.ID_GIOCATORE);
    this.idGiocatore = idGiocatore;
  }

  public int idGiocatore() {
    return idGiocatore;
  }
}
