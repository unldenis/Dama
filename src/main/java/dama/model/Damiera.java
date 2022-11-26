package dama.model;

import dama.Constanti;
import java.util.Arrays;

/**
 * Ciascun client e server devono avere una damiera, tuttavia i movimenti vengono autorizzati dal
 * Server. Questa classe si occupa dei movimenti del gioco della Dama dei pedoni, quindi muovere,
 * autorizzare, mangiare ecc.
 * <a href="https://it.wikipedia.org/wiki/Dama#/media/File:Damiera.JPG">Immagine</a> di
 * riferimento.
 */
public class Damiera {

  protected final Cella[][] celle = new Cella[8][8];
  public int pedoniBianchi = 12, pedoniNeri = 12;

  public void crea() {
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {

        var cella = new Cella(new Punto(x, y), (y + x) % 2 == 0 ? Colore.NERO : Colore.BIANCO);

        celle[y][x] = cella;

        var colore = y < 3 ? Colore.NERO : Colore.BIANCO;

        if (y < 3 || y > 4) {
          if (cella.getColore() == Colore.NERO) {
            var pedone = new Pedone(colore);
            cella.setPedone(pedone);
            pedone.setCellaCorrente(cella);
          }
        }
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

  private void muovi(Pedone pedone, Movimento movimento) {
    var partenza = pedone.getCellaCorrente();
    var destinazione = cellaInPosizione(partenza.getX() + movimento.getMovX(),
        partenza.getY() + movimento
            .getMovY());

    var destPed = destinazione.getPedone();
    if (destPed != null) {

      // controlla che nella cella successiva non ci siano pedoni
      var destinazioneSuccessiva = cellaInPosizione(destinazione.getX() + movimento.getMovX(),
          destinazione.getY() + movimento.getMovY());
      if (destinazioneSuccessiva.getPedone() == null) {
        // mangia pedone avversario
        this.muovi(destinazioneSuccessiva, pedone, partenza);

        destPed.setCellaCorrente(null);
        destinazione.setPedone(null);
      }
    } else {
      this.muovi(destinazione, pedone, partenza);
    }
  }

  private boolean coordinateInvalide(int destX, int destY) {
    return destX > 7 || destY > 7 || destX < 0 || destY < 0;
  }

  public ResultMovimento puoMuoversi(Pedone pedone, Movimento movimento, boolean simula) {
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

    var partenza = pedone.getCellaCorrente();
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
          this.muovi(destinazioneSuccessiva, pedone, partenza);

          destPed.setCellaCorrente(null);
          destinazione.setPedone(null);
        }

        // mangia pedone avversario
        return ResultMovimento.ok(true);
      }

      // nella cella successiva ci sono pedoni
      return ResultMovimento.err("Salto occupato");
    } else {

      if(!simula) {
        this.muovi(destinazione, pedone, partenza);
      }
      return ResultMovimento.ok(false);
    }
  }


  private void muovi(Cella destinazione, Pedone pedone, Cella partenza) {
    destinazione.setPedone(pedone);
    pedone.setCellaCorrente(destinazione);

    partenza.setPedone(null);

    if (destinazione.getY() == 0 && pedone.getColore() == Colore.BIANCO) {
      pedone.setDamone(true);
//      System.out.printf("Pedone %s e' diventato un damone%n", pedone);
    } else if (destinazione.getY() == 7 && pedone.getColore() == Colore.NERO) {
      pedone.setDamone(true);
//      System.out.printf("Pedone %s e' diventato un damone%n", pedone);
    }

  }


}
