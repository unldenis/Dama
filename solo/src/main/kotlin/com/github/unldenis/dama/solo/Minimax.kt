package com.github.unldenis.dama.solo

import com.github.unldenis.dama.api.model.Colore
import com.github.unldenis.dama.api.model.Damiera
import com.github.unldenis.dama.api.model.Movimento
import com.github.unldenis.dama.solo.gui.Gioco


//fun Damiera.valutaPosizione(colore: Colore) =
//    if (colore === Colore.BIANCO) punteggioBianchi - punteggioNeri else punteggioNeri - punteggioBianchi

fun Damiera.valuta(colore: Colore): Int {
    var valore = 0;
    for ((_, cella) in pedineCella) {
        cella.pedone?.let {

            // posizione piu' centrata meglio
            val posizione = when (cella.x) {
                0, 7 -> 0
                1, 6 -> 25
                2, 5 -> 50
                3, 4 -> 75
                else -> throw IllegalStateException()
            }

            // i pedoni della prima fila
            if ((it.colore === Colore.NERO && cella.y == 0) || (it.colore === Colore.BIANCO && cella.y == 7)) {
                valore += if (it.colore == colore) 25 else -25
            }

            // i pedoni del colore del parametro
            if (it.colore == colore) {
                valore += posizione
                valore += it.valore
            } else {
                valore -= posizione;
                valore -= it.valore
            }
        }
    }

    return valore;
}

fun Damiera.movimentiPossibili(coloreGiocatore: Colore): Set<Pair<Int, Movimento>> {
    val possibiliMovimenti = HashSet<Pair<Int, Movimento>>()

    for ((_, cella) in pedineCella) {
        cella.pedone?.let {
            if (it.colore !== coloreGiocatore) {
                return@let
            }
            Movimento.values()
                .filter { mov -> muovi(cella, mov, true).movimentoAutorizzato() }
                .forEach { mov -> possibiliMovimenti.add(Pair(it.id, mov)) }
        }
    }
    return possibiliMovimenti
}

fun minimax(
    profondita: Int,
    dam: Damiera,
    coloreGiocatore: Colore,
    isMaximizingPlayer: Boolean = true,
): Triple<Int, Int?, Movimento?> {
    if (profondita == 0) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(valore, null, null)
    }

    val movPossibili =
        dam.movimentiPossibili(coloreGiocatore = if (isMaximizingPlayer) Colore.NERO else Colore.BIANCO)

    if (movPossibili.isEmpty()) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(valore, null, null)
    }

    var miglioreGiocata: Pair<Int, Movimento>? = null;
    var miglioreValore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE;

    for ((idPedone, movimento) in movPossibili) {
        dam.muovi(dam.cercaCella(idPedone), movimento, false)

        val valore = minimax(
            profondita = profondita - 1,
            dam = dam,
            coloreGiocatore = coloreGiocatore,
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
        dam.undo()
    }

    return if (miglioreGiocata == null) {
        val primoElemento = movPossibili.first();
        Triple(miglioreValore, primoElemento.first, primoElemento.second)
    } else {
        Triple(miglioreValore, miglioreGiocata.first, miglioreGiocata.second)
    }
}

fun debugMinimax(
    profondita: Int,
    dam: Damiera,
    coloreGiocatore: Colore,
    isMaximizingPlayer: Boolean = true,
    pannello: Gioco
): Triple<Nodo, Int?, Movimento?> {

    if (profondita == 0) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(Nodo(valore, "", emptyList()), null, null)
    }

    val movPossibili =
        dam.movimentiPossibili(coloreGiocatore = if (isMaximizingPlayer) Colore.NERO else Colore.BIANCO)

    if (movPossibili.isEmpty()) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(Nodo(valore, "", emptyList()), null, null)
    }

    var miglioreGiocata: Pair<Int, Movimento>? = null;
    var miglioreValore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE;


    val nodi = ArrayList<Nodo>()
    for ((idPedone, movimento) in movPossibili) {
        dam.muovi(dam.cercaCella(idPedone), movimento, false)

        val valore = debugMinimax(
            profondita = profondita - 1,
            dam = dam,
            coloreGiocatore = coloreGiocatore,
            isMaximizingPlayer = !isMaximizingPlayer,
            pannello = pannello
        ).first

        valore.valore.let {
            nodi.add(Nodo(it, "$idPedone : $movimento", valore.children))

            if (isMaximizingPlayer) {
                if (it > miglioreValore) {
                    miglioreValore = it;
                    miglioreGiocata = Pair(idPedone, movimento)
                }
            } else {
                if (it < miglioreValore) {
                    miglioreValore = it;
                    miglioreGiocata = Pair(idPedone, movimento)
                }
            }
        }
        dam.undo()
    }

    return if (miglioreGiocata == null) {
        val primoElemento = movPossibili.first()
        Triple(
            Nodo(miglioreValore, "${primoElemento.first} - ${primoElemento.second}", nodi),
            primoElemento.first,
            primoElemento.second
        )
    } else {
        Triple(
            Nodo(
                miglioreValore,
                "${miglioreGiocata!!.first} - ${miglioreGiocata!!.second}",
                nodi
            ), miglioreGiocata!!.first, miglioreGiocata!!.second
        )
    }
}


fun alphaBetaPruning(
    profondita: Int,
    dam: Damiera,
    coloreGiocatore: Colore,
    isMaximizingPlayer: Boolean = true,
    _alpha: Int = Int.MIN_VALUE,
    _beta: Int = Int.MAX_VALUE
): Triple<Int, Int?, Movimento?> {
    if (profondita == 0) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(valore, null, null)
    }

    val movPossibili =
        dam.movimentiPossibili(coloreGiocatore = if (isMaximizingPlayer) Colore.NERO else Colore.BIANCO)

    if (movPossibili.isEmpty()) {
        val valore = dam.valuta(colore = coloreGiocatore);
        return Triple(valore, null, null)
    }

    var miglioreGiocata: Pair<Int, Movimento>? = null;
    var miglioreValore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE;

    var alpha = _alpha;
    var beta = _beta;

    for ((idPedone, movimento) in movPossibili) {
        dam.muovi(dam.cercaCella(idPedone), movimento, false)

        val valore = alphaBetaPruning(
            profondita = profondita - 1,
            dam = dam,
            coloreGiocatore = coloreGiocatore,
            isMaximizingPlayer = !isMaximizingPlayer,
            _alpha = alpha,
            _beta = beta
        ).first

        if (isMaximizingPlayer) {
            if (valore > miglioreValore) {
                miglioreValore = valore;
                miglioreGiocata = Pair(idPedone, movimento)
            }
            alpha = alpha.coerceAtLeast(valore) // Equivalente Math.max(alpha, valore)
        } else {
            if (valore < miglioreValore) {
                miglioreValore = valore;
                miglioreGiocata = Pair(idPedone, movimento)
            }
            beta = beta.coerceAtMost(valore) // Equivalente Math.min(beta, valore)
        }
        dam.undo()

        // Check for alpha beta pruning
        if (beta <= alpha) {
            break;
        }
    }

    return if (miglioreGiocata == null) {
        val primoElemento = movPossibili.first();
        Triple(miglioreValore, primoElemento.first, primoElemento.second)
    } else {
        Triple(miglioreValore, miglioreGiocata.first, miglioreGiocata.second)
    }
}