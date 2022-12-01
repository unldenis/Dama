package com.github.unldenis.dama.solo.gui.stati

import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.solo.alphaBetaPruning
import com.github.unldenis.dama.solo.gui.Gioco
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
            Thread.sleep(1500L)

            val dam = gioco.damiera!!

            if (dam.movimentiPossibili(Colore.NERO).isEmpty()) {
                JOptionPane.showMessageDialog(gioco, "Il bot ha terminato le mosse, hai vinto")
                return@Thread
            }
            //debug
//            var x = debugMinimax(gioco.difficolta, dam, Colore.NERO, pannello = gioco)
////            println(x)
//            File("debug.txt").writeText(x.toString())
//
//            if(dam.muovi(dam.cercaCella(x.second!!), x.third!!, false).movimentoAutorizzato()) {
//                gioco.setStatoCorrente(InGioco00Stato())
//            }

            val (_, idPedone, movimento) = alphaBetaPruning(
                profondita = gioco.difficolta, coloreGiocatore = Colore.NERO, dam = dam
            )

            if (dam.muovi(dam.cercaCella(idPedone!!), movimento!!, false).movimentoAutorizzato()) {
                gioco.setStatoCorrente(InGioco00Stato())
            }
        }.start()
    }

    override fun paint(gioco: Gioco, g: Graphics2D?) {
        gioco.damiera!!.disegna(g)
    }

    override fun mousePressed(gioco: Gioco, e: MouseEvent) {}
}