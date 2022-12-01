package com.github.unldenis.dama.solo.gui

import javax.swing.JFrame

class FinestraGioco(difficolta: Int) : JFrame("Dama Solo") {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        val panello = Gioco(difficolta)
        add(panello)
        pack()

        setLocationRelativeTo(null)
        isVisible = true
    }

}