package com.github.unldenis.dama.solo.gui.stati

import com.github.unldenis.dama.api.Constanti
import com.github.unldenis.dama.api.model.Cella
import com.github.unldenis.dama.api.model.Movimento
import com.github.unldenis.dama.api.model.Punto
import com.github.unldenis.dama.api.model.ResultMovimento.ErrMovimento
import com.github.unldenis.dama.solo.gui.Gioco
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import javax.swing.JOptionPane

/**
 * Lo stato del giocatore sempre nel suo turno, ma dopo aver cliccato un suo pedone. Evidenza la cella
 * selezionata oltre ai possibili movimenti di un pedone.
 */
class InGioco01Stato(private val cellaPedoneSelezionato: Cella) : Stato {

    private var possibiliMovimenti: List<Punto>? = null

    override fun lancia(gioco: Gioco) {
        possibiliMovimenti = Movimento.values()
            .filter {
                gioco.damiera!!.muovi(cellaPedoneSelezionato, it, true).movimentoAutorizzato()
            }
            .map { Punto(cellaPedoneSelezionato.x + it.movX, cellaPedoneSelezionato.y + it.movY) }
            .toList()
        gioco.repaint()
    }

    override fun paint(gioco: Gioco, g: Graphics2D?) {
        val guiDamiera = gioco.damiera
        guiDamiera!!.disegnaEvidenzaCella(g, cellaPedoneSelezionato, possibiliMovimenti)
    }

    override fun mousePressed(gioco: Gioco, e: MouseEvent) {
        val x = e.x / Constanti.LATO_CELLA
        val y = e.y / Constanti.LATO_CELLA
        val cellaPart = cellaPedoneSelezionato
        val cellaDest = gioco.damiera!!.cellaInPosizione(x, y)
        val pedone = cellaDest.pedone
        if (pedone === cellaPedoneSelezionato.pedone) {
            gioco.setStatoCorrente(InGioco00Stato())
        } else {
            val movimento =
                Movimento.ricercaMovimento(cellaDest.x - cellaPart.x, cellaDest.y - cellaPart.y)
            if (movimento == null) {
                JOptionPane.showMessageDialog(
                    gioco,
                    "Movimento non valido",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
                )
                return
            }
            val codice = gioco.damiera!!.muovi(cellaPart, movimento, false)
            if (codice.movimentoAutorizzato()) {
                gioco.setStatoCorrente(InGioco02Stato())
            } else {
                JOptionPane.showMessageDialog(
                    gioco, (codice as ErrMovimento).errore(),
                    "Movimento non valido", JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }
}