package dama.net.packet;

import java.io.Serializable;

/**
 * Un pacchetto è un informazione che viene serializzato e trasmesso dal client al server e
 * viceversa.
 */
public abstract class Pacchetto implements Serializable {

  private final byte packetId;

  public Pacchetto(int packetId) {
    this.packetId = (byte) packetId;
  }

  public Pacchetto(TipoPacchetto tipoPacchetto) {
    this(tipoPacchetto.id());
  }

  public static TipoPacchetto ricercaPacchetto(byte id) {
    for (var t : TipoPacchetto.values()) {
      if (t.id() == id) {
        return t;
      }
    }
    return TipoPacchetto.INVALIDO;
  }

  public byte packetId() {
    return packetId;
  }

}
