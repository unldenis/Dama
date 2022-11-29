package com.github.unldenis.dama.api.net.packet.serverbound;

import com.github.unldenis.dama.api.model.Punto;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto che chiede i possibili movimenti di un pedone al server.
 */
public class PacchettoInPossibiliMovimenti extends PacchettoIn {

  private final Punto cellaPedone;

  public PacchettoInPossibiliMovimenti(Punto cellaPedone) {
    super(TipoPacchetto.POSSIBILI_MOVIMENTI);
    this.cellaPedone = cellaPedone;
  }

  public Punto getCellaPedone() {
    return cellaPedone;
  }
}
