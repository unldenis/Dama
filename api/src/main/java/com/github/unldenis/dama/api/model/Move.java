package com.github.unldenis.dama.api.model;

public interface Move {

  void move(Damiera damiera);

  void undo(Damiera damiera);

}