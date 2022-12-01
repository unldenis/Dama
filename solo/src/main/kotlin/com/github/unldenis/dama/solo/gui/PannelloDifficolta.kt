package com.github.unldenis.dama.solo.gui

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class PannelloDifficolta(finestra: JFrame) : JPanel() {
    init {
        val label = JLabel("Gioca contro il Bot")
        layout = GridBagLayout()
        val gbc = GridBagConstraints()
        gbc.insets = Insets(5, 5, 5, 5)

        gbc.gridx = 0
        gbc.gridy = 0
        add(label, gbc)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridy++

        JButton("Facile").also { add(it, gbc) }.addActionListener {
            finestra.dispose()
            FinestraGioco(1)
        }
        gbc.gridy++

        JButton("Intermedia").also { add(it, gbc) }.addActionListener {
            finestra.dispose()
            FinestraGioco(3)
        }
        gbc.gridy++

        JButton("Difficile").also { add(it, gbc) }.addActionListener {
            finestra.dispose()
            FinestraGioco(5)
        }
        gbc.gridy++
    }
}
