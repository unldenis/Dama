package com.github.unldenis.dama.api.net.packet.clientbound;


import com.github.unldenis.dama.api.model.Punto;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;
import java.util.List;

/**
 * Pacchetto risposta alla richiesta dei possibili movimenti di un pedone.
 */
public class PacchettoOutPossibiliMovimenti extends PacchettoOut {

  private final List<Punto> coordinate;

  public PacchettoOutPossibiliMovimenti(List<Punto> coordinate) {
    super(TipoPacchetto.POSSIBILI_MOVIMENTI);
    this.coordinate = coordinate;
  }

  public List<Punto> getCoordinate() {
    return coordinate;
  }
}
