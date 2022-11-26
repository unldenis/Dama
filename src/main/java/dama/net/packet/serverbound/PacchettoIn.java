package dama.net.packet.serverbound;

import dama.net.packet.Pacchetto;
import dama.net.packet.TipoPacchetto;

/**
 * Un pacchetto che viene inviato dal client al server.
 */
public abstract class PacchettoIn extends Pacchetto {

  public PacchettoIn(int packetId) {
    super(packetId);
  }

  public PacchettoIn(TipoPacchetto tipoPacchetto) {
    super(tipoPacchetto);
  }
}
