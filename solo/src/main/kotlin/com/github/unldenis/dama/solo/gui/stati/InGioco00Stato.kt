package com.github.unldenis.dama.solo.gui.stati

import com.github.unldenis.dama.api.Constanti
import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.solo.gui.Gioco
import java.awt.Graphics2D
import java.awt.event.MouseEvent

/**
 * Lo stato del giocatore nel suo turno.
 */
class InGioco00Stato : Stato {

    override fun lancia(gioco: Gioco) {
        gioco.repaint()
    }

    override fun paint(gioco: Gioco, g: Graphics2D?) {
        gioco.damiera!!.disegna(g)
    }

    override fun mousePressed(gioco: Gioco, e: MouseEvent) {
        val x = e.x / Constanti.LATO_CELLA
        val y = e.y / Constanti.LATO_CELLA
        val cella = gioco.damiera!!.cellaInPosizione(x, y)
        val pedone = cella.pedone
        if (pedone?.colore === Colore.BIANCO) {
            gioco.setStatoCorrente(InGioco01Stato(cella))
        }
    }
}