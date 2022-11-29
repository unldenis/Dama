package com.github.unldenis.dama.solo

import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.api.model.Damiera
import com.github.unldenis.dama.api.model.Movimento
import com.github.unldenis.dama.api.model.ResultMovimento

fun Damiera.valutaPosizione(coloreGiocatore: Colore) =
    if (coloreGiocatore == Colore.BIANCO) punteggioBianchi - punteggioNeri else punteggioNeri - punteggioBianchi

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
    coloreGiocatore: Colore,
    isMaximizingPlayer: Boolean = true
): Triple<Int, Int?, Movimento?> {
    if (profondita == 0) {
        val value = dam.valutaPosizione(coloreGiocatore = coloreGiocatore);
        return Triple(value, null, null)
    }

    val movPossibili = dam.movimentiPossibili(coloreGiocatore = coloreGiocatore)

    var miglioreGiocata: Pair<Int, Movimento>? = null;
    var miglioreValore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE;

    for ((idPedone, movimento) in movPossibili) {
        val copia = dam.clone();

        copia.muovi(copia.cercaCella(idPedone), movimento, false)


        val valore = minimax(
            profondita = profondita - 1,
            dam = copia,
            coloreGiocatore = coloreGiocatore,
            isMaximizingPlayer = !isMaximizingPlayer
        ).first

//        if (profondita == 1) {
//            println("Movimento ${movimento.name} valore = $valore")
//        }

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

fun lanciaMinimax(damiera: Damiera, profondita: Int, coloreGiocatore: Colore): ResultMovimento? {
    val (_, idPedone, movimento) = minimax(
        profondita = profondita,
        coloreGiocatore = coloreGiocatore,
        dam = damiera
    )

    if (idPedone == null || movimento == null) {
        throw NullPointerException()
    }
    return damiera.muovi(damiera.cercaCella(idPedone), movimento, false)
}