package vecchia;

import java.util.Arrays;

public class Damiera {

  private final Cella[][] celle = new Cella[8][8];
  public int pedoniBianchi = 12, pedoniNeri = 12;

  public void crea() {
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {

        var cella = new Cella(x, y, (y + x) % 2 == 0 ? Colore.BIANCO : Colore.NERO);

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
    }
  }

  public Cella cellaInPosizione(int x, int y) {
    return celle[y][x];
  }

  public void muovi(Pedone pedone, Movimento movimento) {
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

  public String puoMuoversi(Pedone pedone, Movimento movimento) {
    if (!pedone.isDamone()) {
      if (pedone.getColore() == Colore.BIANCO) {
        if (movimento == Movimento.SUD_OVEST || movimento == Movimento.SUD_EST) {
          return "Il pedone non è un damone";
        }
      } else {
        //(nero)
        if (movimento == Movimento.NORD_OVEST || movimento == Movimento.NORD_EST) {
          return "Il pedone non è un damone";
        }
      }
    }

    var partenza = pedone.getCellaCorrente();
    var destX = partenza.getX() + movimento.getMovX();
    var destY = partenza.getY() + movimento.getMovY();
    if (coordinateInvalide(destX, destY)) {
      return "vecchia.Movimento fuori dalla damiera";
    }
    var destinazione = cellaInPosizione(destX, destY);

    var destPed = destinazione.getPedone();
    if (destPed != null) {
      if (destPed.getColore() == pedone.getColore()) {
        return "Non puoi tentare di mangiare un pedone della tua stessa squadra";
      }

      if (destPed.isDamone() && !pedone.isDamone()) {
        return "Damone avversario non può essere mangiato da un pedone";
      }

      // controlla che nella cella successiva non ci siano pedoni
      destX = destinazione.getX() + movimento.getMovX();
      destY = destinazione.getY() + movimento.getMovY();
      if (coordinateInvalide(destX, destY)) {
        return "Salto fuori dalla damiera";
      }
      var destinazioneSuccessiva = cellaInPosizione(destX, destY);
      if (destinazioneSuccessiva.getPedone() == null) {

        // mangia pedone avversario
        return "mangia";
      }

      // nella cella successiva ci sono pedoni
      return "Salto occupato";
    } else {
      return "";
    }
  }

  private void muovi(Cella destinazione, Pedone pedone, Cella partenza) {
    destinazione.setPedone(pedone);
    pedone.setCellaCorrente(destinazione);

    partenza.setPedone(null);

    if (destinazione.getY() == 0 && pedone.getColore() == Colore.BIANCO) {
      pedone.setDamone(true);
      System.out.printf("vecchia.Pedone %s e' diventato un damone%n", pedone);
    } else if (destinazione.getY() == 7 && pedone.getColore() == Colore.NERO) {
      pedone.setDamone(true);
      System.out.printf("vecchia.Pedone %s e' diventato un damone%n", pedone);
    }

  }


}
