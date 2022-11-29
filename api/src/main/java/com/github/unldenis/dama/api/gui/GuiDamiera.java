package com.github.unldenis.dama.api.gui;

import com.github.unldenis.dama.api.model.Cella;
import com.github.unldenis.dama.api.model.Colore;
import com.github.unldenis.dama.api.model.Damiera;
import com.github.unldenis.dama.api.model.Punto;
import com.github.unldenis.dama.api.Constanti;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Una dama.model.Damiera che può essere rappresentata, eventualmente specchiata, tramite
 * Graphics2D.
 */
public class GuiDamiera extends Damiera {

  private static final BufferedImage damaBiancaImg, damaNeraImg, damoneBiancoImg, damoneNeroImg;


  static {
    var cl = GuiDamiera.class;
    try {
      damaBiancaImg = caricaImmagine("DamaBianca");
      damaNeraImg = caricaImmagine("DamaNera");
      damoneBiancoImg = caricaImmagine("DamoneBianco");
      damoneNeroImg = caricaImmagine("DamoneNero");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final boolean specchiato;

  public GuiDamiera(Colore coloreGiocatore) {
    this.specchiato = coloreGiocatore == Colore.NERO;
  }

  private static BufferedImage caricaImmagine(String immagine) throws IOException {
    return ImageIO.read(Objects.requireNonNull(
        GuiDamiera.class.getResource("/immagini/%s.png".formatted(immagine))));
  }

  public void disegna(Graphics2D g) {
    disegnaEvidenzaCella(g, null, null);
  }

  public void disegnaEvidenzaCella(Graphics2D g, Cella cellaPedone,
      List<Punto> possibiliMovimenti) {

    for (var riga : celle) {
      for (var cella : riga) {
        final int xD =
            (specchiato ? cella.getCoordinata().xSpec() : cella.getX()) * Constanti.LATO_CELLA;
        final int yD =
            (specchiato ? cella.getCoordinata().ySpec() : cella.getY()) * Constanti.LATO_CELLA;

        var colore = cella.getColore();

        if (cellaPedone != null && cellaPedone == cella) {
          g.setColor(Color.GREEN);
        } else {
          g.setColor(colore.coloreAwt());
        }

        g.fillRect(xD, yD, Constanti.LATO_CELLA, Constanti.LATO_CELLA);

        // debug coordinate
//        g.setColor(Color.BLUE);
//        g.drawString(cella.formatId(), xD + 10, yD + 10);

        var p = cella.getPedone();
        if (p != null) {

          BufferedImage immagine;
          if (p.isDamone()) {
            immagine = p.getColore() == Colore.NERO ? damoneNeroImg : damoneBiancoImg;
          } else {
            immagine = p.getColore() == Colore.NERO ? damaNeraImg : damaBiancaImg;
          }

          g.drawImage(immagine, xD + 10, yD + 10, null);
        }

        if (possibiliMovimenti != null && possibiliMovimenti.contains(cella.getCoordinata())) {
          g.setColor(Color.GREEN);
          g.fillOval(xD + 20, yD + 20, 20, 20);
        }
      }
    }
  }

}
