package dama.net.packet.serverbound;

import dama.model.Punto;
import dama.net.packet.TipoPacchetto;

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
