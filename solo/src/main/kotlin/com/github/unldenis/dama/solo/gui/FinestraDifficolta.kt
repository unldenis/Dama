package com.github.unldenis.dama.solo.gui

import com.github.unldenis.dama.api.gui.FinestraConsole
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class FinestraDifficolta : JFrame("Dama") {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false
        setSize(677, 343)

        prepara()

        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun prepara() {
        val pnl = JPanel(GridLayout())
        JButton("Facile").also { pnl.add(it) }.addActionListener {
            dispose()
            FinestraGioco(1)
        }

        JButton("Intermedia").also { pnl.add(it) }.addActionListener {
            dispose()
            FinestraGioco(3)
        }

        JButton("Difficile").also { pnl.add(it) }.addActionListener {
            dispose()
            FinestraGioco(3)
        }

        add(pnl)
    }

}