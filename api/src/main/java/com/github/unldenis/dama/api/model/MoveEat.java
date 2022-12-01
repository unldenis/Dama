package com.github.unldenis.dama.api.model;

public class MoveEat extends MoveBasic {

  private final Pedone pedoneMangiato;
  private final Cella cellaPedoneMangiato;

  public MoveEat(Pedone pedone, Cella partenza, Cella destinazioneSuccessiva,
      Pedone pedoneMangiato, Cella cellaPedoneMangiato) {
    super(pedone, partenza, destinazioneSuccessiva);
    this.pedoneMangiato = pedoneMangiato;
    this.cellaPedoneMangiato = cellaPedoneMangiato;
  }

  @Override
  public void move(Damiera damiera) {
    super.move(damiera);

    cellaPedoneMangiato.setPedone(null);

    if (pedoneMangiato.getColore() == Colore.BIANCO) {
      damiera.pedoniBianchi--;

      damiera.punteggioBianchi -= pedoneMangiato.getValore();
    } else {
      damiera.pedoniNeri--;

      damiera.pedoniNeri -= pedoneMangiato.getValore();
    }

    damiera.pedineCella.remove(pedoneMangiato.getId());

  }

  @Override
  public void undo(Damiera damiera) {
    super.undo(damiera);

    cellaPedoneMangiato.setPedone(pedoneMangiato);

    if (pedoneMangiato.getColore() == Colore.BIANCO) {
      damiera.pedoniBianchi++;

      damiera.punteggioBianchi += pedoneMangiato.getValore();
    } else {
      damiera.pedoniNeri++;

      damiera.pedoniNeri += pedoneMangiato.getValore();
    }

    damiera.pedineCella.put(pedoneMangiato.getId(), cellaPedoneMangiato);
  }
}
