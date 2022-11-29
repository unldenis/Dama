package com.github.unldenis.dama.solo

import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.api.model.Damiera
import com.github.unldenis.dama.api.model.Movimento
import java.util.*

fun main() {
    val damiera = Damiera()
    damiera.crea()

    damiera.stampa()
    println()

    val scanner = Scanner(System.`in`)

    while (true) {
        damiera.muovi(
            damiera.cellaInPosizione(scanner.nextInt(), scanner.nextInt()),
            Movimento.valueOf(scanner.next().uppercase()),
            false
        )

        damiera.stampa()
        println()

        mossaRobot(damiera)


    }
}

fun mossaRobot(damiera: Damiera) {
    val (valoreGiocata, idPedone, movimento) = minimax(
        profondita = 1,
        dam = damiera
    )

    if (idPedone == null || movimento == null) {
        throw NullPointerException()
    }

    println("Movimento bot '${movimento.name}', con valore '$valoreGiocata'")
    damiera.muovi(damiera.cercaCella(idPedone), movimento, false)

    damiera.stampa()
    println()
}


fun Damiera.valutaPosizione(isMaximizingPlayer: Boolean) =
    if (isMaximizingPlayer) pedoniBianchi - pedoniNeri else pedoniNeri - pedoniBianchi

fun Damiera.movimentiPossibili(coloreGiocatore: Colore): Set<Pair<Int, Movimento>> {
    val possibiliMovimenti = HashSet<Pair<Int, Movimento>>()
    for (riga in celle) {
        for (cella in riga) {
            cella.pedone?.let {
                if (it.colore !== coloreGiocatore) {
                    return@let
                }
                Movimento.values()
                    .filter { mov -> muovi(cella, mov, true).movimentoAutorizzato() }
                    .forEach { mov -> possibiliMovimenti.add(Pair(it.id, mov)) }
            }
        }
    }
    return possibiliMovimenti
}

fun minimax(
    profondita: Int,
    dam: Damiera,
    isMaximizingPlayer: Boolean = true
): Triple<Int, Int?, Movimento?> {
    if (profondita == 0) {
        val value = -dam.valutaPosizione(isMaximizingPlayer = isMaximizingPlayer);
        return Triple(value, null, null)
    }

    val movPossibili =
        dam.movimentiPossibili(coloreGiocatore = if (isMaximizingPlayer) Colore.BIANCO else Colore.NERO)

    var miglioreGiocata: Pair<Int, Movimento>? = null;
    var miglioreValore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE;

    for ((idPedone, movimento) in movPossibili) {
        val copia = dam.clone();

        copia.muovi(copia.cercaCella(idPedone), movimento, false)


        val valore = minimax(
            profondita = profondita - 1,
            dam = copia,
            isMaximizingPlayer = !isMaximizingPlayer
        ).first

        if (isMaximizingPlayer) {
            if (valore > miglioreValore) {
                miglioreValore = valore;
                miglioreGiocata = Pair(idPedone, movimento)
            }
        } else {
            if (valore < miglioreValore) {
                miglioreValore = valore;
                miglioreGiocata = Pair(idPedone, movimento)
            }
        }
    }
    return if (miglioreGiocata == null) {
        val primoElemento = movPossibili.first()
        Triple(miglioreValore, primoElemento.first, primoElemento.second)
    } else {
        Triple(miglioreValore, miglioreGiocata.first, miglioreGiocata.second)
    }

}