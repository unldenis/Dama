package com.github.unldenis.dama.api.net.packet.serverbound;

import com.github.unldenis.dama.api.net.packet.Pacchetto;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

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
