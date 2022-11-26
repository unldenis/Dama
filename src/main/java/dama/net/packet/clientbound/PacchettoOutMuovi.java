package dama.net.packet.clientbound;

import dama.model.Movimento;
import dama.model.Punto;
import dama.model.ResultMovimento;
import dama.net.packet.TipoPacchetto;

/**
 * Pacchetto risposta alla richiesta di un movimento da parte di yn pedone.
 */
public class PacchettoOutMuovi extends PacchettoOut {

  private final Punto cellaPedone;
  private final Movimento movimento;
  private final ResultMovimento resultMovimento;

  public PacchettoOutMuovi(Punto cellaPedone, Movimento movimento,
      ResultMovimento resultMovimento) {
    super(TipoPacchetto.MUOVI);
    this.cellaPedone = cellaPedone;
    this.movimento = movimento;
    this.resultMovimento = resultMovimento;
  }

  public Punto getCellaPedone() {
    return cellaPedone;
  }

  public Movimento getMovimento() {
    return movimento;
  }

  public ResultMovimento getResultMovimento() {
    return resultMovimento;
  }
}
