package com.github.unldenis.dama.api.model;

import java.io.Serializable;

/**
 * Tramite questa <a href="https://it.wikipedia.org/wiki/Dama#/media/File:Damiera.JPG">immagine</a>
 * di riferimento si possono distinguere i diversi movimenti dei pedoni. L'origine degli assi è in
 * alto a sinistra.
 */
public enum Movimento implements Serializable {

  NORD_OVEST(-1, -1),
  NORD_EST(1, -1),
  SUD_OVEST(-1, 1),
  SUD_EST(1, 1),

  ;

  private final int movX;
  private final int movY;


  Movimento(int movX, int movY) {
    this.movX = movX;
    this.movY = movY;
  }

  public static Movimento ricercaMovimento(int deltaX, int deltaY) {
    for (var m : Movimento.values()) {
      if (m.getMovX() == deltaX && m.getMovY() == deltaY) {
        return m;
      }
    }
    return null;
  }

  public int getMovX() {
    return movX;
  }

  public int getMovY() {
    return movY;
  }

  //private static BestMove calcBestMoveNoAB(int depth, Damiera game, boolean isMaximizingPlayer) {
//    if (depth == 0) {
//        var value = -valutaPosizione(game, isMaximizingPlayer);
//        return new BestMove(value, null);
//    }
//    MovimentoPedone bestMove = null;
//
//    var possibleMoves = new ArrayList<MovimentoPedone>();
//    moves(game, possibleMoves, isMaximizingPlayer ? Colore.BIANCO : Colore.NERO);
//
//    Collections.shuffle(possibleMoves);
//
//    // Set a default best move value
//    var bestMoveValue = isMaximizingPlayer ? Integer.MIN_VALUE
//    : Integer.MAX_VALUE;
//
//    for (var move : possibleMoves) {
//        var copia = game.clone();
//        copia.muovi(copia.cercaCella(move.idPedone()), move.movimento(), false);
//        var value = calcBestMoveNoAB(depth - 1, copia, !isMaximizingPlayer).value;
//
////      System.out.printf("Valore movimento %s: %d%n", move, value);
//
//        if (isMaximizingPlayer) {
//            if (value > bestMoveValue) {
//                bestMoveValue = value;
//                bestMove = move;
//            }
//        } else {
//            if (value < bestMoveValue) {
//                bestMoveValue = value;
//                bestMove = move;
//            }
//        }
//    }
//    return new BestMove(bestMoveValue, bestMove != null ? bestMove : possibleMoves.get(0));
//}
}
