package com.github.unldenis.dama.api.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Ciascun client e server devono avere una damiera, tuttavia i movimenti vengono autorizzati dal
 * Server. Questa classe si occupa dei movimenti del gioco della Dama dei pedoni, quindi muovere,
 * autorizzare, mangiare ecc.
 * <a href="https://it.wikipedia.org/wiki/Dama#/media/File:Damiera.JPG">Immagine</a> di
 * riferimento.
 */
public class Damiera {

  protected final Cella[][] celle = new Cella[8][8];
  protected final Map<Integer, Cella> pedineCella = new HashMap<>();
  protected int pedoniBianchi = 12, pedoniNeri = 12;

  public void crea() {
    int pedineId = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {

        var cella = new Cella(new Punto(x, y), (y + x) % 2 == 0 ? Colore.NERO : Colore.BIANCO);

        var colore = y < 3 ? Colore.NERO : Colore.BIANCO;

        if (y < 3 || y > 4) {
          if (cella.getColore() == Colore.NERO) {
            var pedone = new Pedone(++pedineId, colore);
            cella.setPedone(pedone);

            pedineCella.put(pedone.getId(), cella);
          }
        }

        celle[y][x] = cella;
      }
    }
  }

  public void stampa() {
    for (var riga : celle) {
      System.out.println(Arrays.toString(riga));
//      System.out.println(
//          Arrays.toString(Arrays.stream(riga)
//          .map(cella -> cella.getColore().name().charAt(0))
//          .toArray(Character[]::new)));

    }
  }

  public Cella cellaInPosizione(int x, int y) {
    return celle[y][x];
  }

  private boolean coordinateInvalide(int destX, int destY) {
    return destX > 7 || destY > 7 || destX < 0 || destY < 0;
  }

  public Cella cercaCella(int idPedone) {
    return pedineCella.get(idPedone);
  }

  public ResultMovimento muovi(Cella partenza, Movimento movimento, boolean simula) {
    var pedone = partenza.getPedone();
    if (!pedone.isDamone()) {
      if (pedone.getColore() == Colore.BIANCO) {
        if (movimento == Movimento.SUD_OVEST || movimento == Movimento.SUD_EST) {
          return ResultMovimento.err("Il pedone non è un damone");
        }
      } else {
        //(nero)
        if (movimento == Movimento.NORD_OVEST || movimento == Movimento.NORD_EST) {
          return ResultMovimento.err("Il pedone non è un damone");
        }
      }
    }

    var destX = partenza.getX() + movimento.getMovX();
    var destY = partenza.getY() + movimento.getMovY();
    if (coordinateInvalide(destX, destY)) {
      return ResultMovimento.err("Movimento fuori dalla damiera");
    }
    var destinazione = cellaInPosizione(destX, destY);

    var destPed = destinazione.getPedone();
    if (destPed != null) {
      if (destPed.getColore() == pedone.getColore()) {
        return ResultMovimento.err("Non puoi tentare di mangiare un pedone della tua stessa squadra");
      }

      if (destPed.isDamone() && !pedone.isDamone()) {
        return ResultMovimento.err("Damone avversario non può essere mangiato da un pedone");
      }

      // controlla che nella cella successiva non ci siano pedoni
      destX = destinazione.getX() + movimento.getMovX();
      destY = destinazione.getY() + movimento.getMovY();
      if (coordinateInvalide(destX, destY)) {
        return ResultMovimento.err("Salto fuori dalla damiera");
      }
      var destinazioneSuccessiva = cellaInPosizione(destX, destY);
      if (destinazioneSuccessiva.getPedone() == null) {

        if(!simula) {
          // mangia pedone avversario
          this.muoviPedone(pedone, partenza, destinazioneSuccessiva);

          destinazione.setPedone(null);

          if(destPed.getColore() == Colore.BIANCO) {
            pedoniBianchi--;
          } else {
            pedoniNeri--;
          }

          pedineCella.remove(destPed.getId());
        }

        // mangia pedone avversario
        return ResultMovimento.ok(true);
      }

      // nella cella successiva ci sono pedoni
      return ResultMovimento.err("Salto occupato");
    } else {

      if(!simula) {
        this.muoviPedone(pedone, partenza, destinazione);
      }
      return ResultMovimento.ok(false);
    }
  }


  private void muoviPedone(Pedone pedone, Cella partenza, Cella destinazione) {
    pedineCella.put(pedone.getId(), destinazione);

    destinazione.setPedone(pedone);

    partenza.setPedone(null);

    if (destinazione.getY() == 0 && pedone.getColore() == Colore.BIANCO) {
      pedone.setDamone(true);
//      System.out.printf("Pedone %s e' diventato un damone%n", pedone);
    } else if (destinazione.getY() == 7 && pedone.getColore() == Colore.NERO) {
      pedone.setDamone(true);
//      System.out.printf("Pedone %s e' diventato un damone%n", pedone);
    }
  }
  public Damiera clone() {
    var d = new Damiera();
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        var c = this.celle[y][x].clone();

        if(c.getPedone() != null) {
          d.pedineCella.put(c.getPedone().getId(), c);
        }

        d.celle[y][x] = c;
      }
    }
    d.pedoniBianchi = this.pedoniBianchi;
    d.pedoniNeri = this.pedoniNeri;
    return d;
  }

  public Cella[][] getCelle() {
    return celle;
  }

  public int getPedoniBianchi() {
    return pedoniBianchi;
  }

  public int getPedoniNeri() {
    return pedoniNeri;
  }
}
