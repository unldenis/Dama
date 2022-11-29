package com.github.unldenis.dama.solo.gui.stati

import com.github.unldenis.dama.solo.gui.Gioco
import java.awt.Graphics2D
import java.awt.event.MouseEvent

/**
 * Classe che rappresenta uno stato per il giocatore.
 */
interface Stato {

    fun lancia(gioco: Gioco)

    fun paint(gioco: Gioco, g: Graphics2D?)

    fun mousePressed(gioco: Gioco, e: MouseEvent)

}