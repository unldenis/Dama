package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto inviato in broadcast per far iniziare la partita.(secondo giocatore collegato)
 */
public class PacchettoOutIniziaPartita extends PacchettoOut {

  public PacchettoOutIniziaPartita() {
    super(TipoPacchetto.INIZIA_PARTITA);
  }

}
