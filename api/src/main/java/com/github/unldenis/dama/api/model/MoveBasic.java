package com.github.unldenis.dama.api.model;

public class MoveBasic implements Move {

  private final Pedone pedone;
  private final Cella partenza, destinazione;

  public MoveBasic(Pedone pedone, Cella partenza, Cella destinazione) {
    this.pedone = pedone;
    this.partenza = partenza;
    this.destinazione = destinazione;
  }

  private Cella vecchiaCella;
  private boolean eraDamone;

  @Override
  public void move(Damiera damiera) {
    // informazioni backup
    vecchiaCella = damiera.pedineCella.get(pedone.getId());
    eraDamone = pedone.isDamone();
    //

    damiera.pedineCella.put(pedone.getId(), destinazione);

    destinazione.setPedone(pedone);
    partenza.setPedone(null);

    if(!eraDamone) {
      if (destinazione.getY() == 0 && pedone.getColore() == Colore.BIANCO) {
        damiera.punteggioBianchi -= pedone.getValore();
        pedone.setDamone(true);
        damiera.punteggioBianchi += pedone.getValore();
      } else if (destinazione.getY() == 7 && pedone.getColore() == Colore.NERO) {
        damiera.punteggioNeri -= pedone.getValore();
        pedone.setDamone(true);
        damiera.punteggioNeri += pedone.getValore();
      }
    }

  }

  @Override
  public void undo(Damiera damiera) {
    damiera.pedineCella.put(pedone.getId(), vecchiaCella);

    destinazione.setPedone(null);
    partenza.setPedone(pedone);

    if(!eraDamone && pedone.isDamone()) {
      if (pedone.getColore() == Colore.BIANCO) {
        pedone.setDamone(false);
        damiera.punteggioBianchi -= 2;
      } else if (pedone.getColore() == Colore.NERO) {
        pedone.setDamone(false);
        damiera.punteggioNeri -= 2;
      }
    }

  }
}
