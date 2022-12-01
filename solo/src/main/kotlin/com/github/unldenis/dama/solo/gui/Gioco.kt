package com.github.unldenis.dama.solo.gui

import com.github.unldenis.dama.api.Constanti
import com.github.unldenis.dama.api.gui.GuiDamiera
import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.solo.gui.stati.InGioco02Stato
import com.github.unldenis.dama.solo.gui.stati.Stato
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel

/**
 * Classe che gestisce la partita.
 */
class Gioco(val difficolta: Int) : JPanel(), MouseListener {
    var damiera: GuiDamiera? = null
    private var statoCorrente: Stato? = null

    init {
        GuiDamiera(Colore.BIANCO).also { damiera = it }.crea()
        preferredSize = Dimension(Constanti.LATO_FINESTRA, Constanti.LATO_FINESTRA)
        isDoubleBuffered = true
        addMouseListener(this)
        isFocusable = true
        setStatoCorrente(InGioco02Stato())
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        statoCorrente!!.paint(this, g2)
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {
        statoCorrente!!.mousePressed(this, e)
    }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}

    fun setStatoCorrente(statoCorrente: Stato) {
        statoCorrente.also { this.statoCorrente = it }.lancia(this)
    }
}