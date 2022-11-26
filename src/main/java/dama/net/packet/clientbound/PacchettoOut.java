package dama.net.packet.clientbound;

import dama.net.packet.Pacchetto;
import dama.net.packet.TipoPacchetto;

/**
 * Un pacchetto che viene inviato dal server al client.
 */
public abstract class PacchettoOut extends Pacchetto {

  public PacchettoOut(int packetId) {
    super(packetId);
  }

  public PacchettoOut(TipoPacchetto tipoPacchetto) {
    super(tipoPacchetto);
  }
}
