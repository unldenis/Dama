package com.github.unldenis.dama.api.net.packet.clientbound;

import com.github.unldenis.dama.api.net.packet.Pacchetto;
import com.github.unldenis.dama.api.net.packet.TipoPacchetto;

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
