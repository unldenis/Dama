package com.github.unldenis.dama.solo.gui.stati

import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.solo.gui.Gioco
import com.github.unldenis.dama.solo.lanciaMinimax
import com.github.unldenis.dama.solo.movimentiPossibili
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import javax.swing.JOptionPane

/**
 * Turno del bot.
 */
class InGioco02Stato : Stato {
    override fun lancia(gioco: Gioco) {
        gioco.repaint()
        Thread {
            try {
                Thread.sleep(1500L)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

            val dam = gioco.damiera!!

            if (dam.movimentiPossibili(Colore.NERO).isEmpty()) {
                JOptionPane.showMessageDialog(gioco, "Il bot ha terminato le mosse, hai vinto")
                return@Thread
            }

            val codice = lanciaMinimax(dam, gioco.difficolta, Colore.NERO)
            if (codice!!.movimentoAutorizzato()) {
                gioco.setStatoCorrente(InGioco00Stato())
            }
        }.start()
    }

    override fun paint(gioco: Gioco, g: Graphics2D?) {
        gioco.damiera!!.disegna(g)
    }

    override fun mousePressed(gioco: Gioco, e: MouseEvent) {}
}