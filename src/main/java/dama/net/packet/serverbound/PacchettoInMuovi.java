package dama.net.packet.serverbound;

import dama.model.Movimento;
import dama.model.Punto;
import dama.net.packet.TipoPacchetto;

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
