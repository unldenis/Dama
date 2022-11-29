package com.github.unldenis.dama.api.net.packet.serverbound;

import com.github.unldenis.dama.api.model.Movimento;
import com.github.unldenis.dama.api.model.Punto;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

/**
 * Pacchetto che chiede un movimento da parte di un pedone al server.
 */
public class PacchettoInMuovi extends PacchettoIn {

  private final Punto cellaPedone;
  private final Movimento movimento;

  public PacchettoInMuovi(Punto cellaPedone, Movimento movimento) {
    super(TipoPacchetto.MUOVI);
    this.cellaPedone = cellaPedone;
    this.movimento = movimento;
  }

  public Punto getCellaPedone() {
    return cellaPedone;
  }

  public Movimento getMovimento() {
    return movimento;
  }
}
