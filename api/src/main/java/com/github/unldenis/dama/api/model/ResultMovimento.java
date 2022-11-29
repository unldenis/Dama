package com.github.unldenis.dama.api.model;


import java.io.Serializable;

public interface ResultMovimento extends Serializable {

  static ResultMovimento ok(boolean mangia) {
    return new OkMovimento(mangia);
  }

  static ResultMovimento err(String errore) {
    return new ErrMovimento(errore);
  }

  boolean movimentoAutorizzato();

  record OkMovimento(boolean mangia) implements ResultMovimento {


    @Override
    public boolean movimentoAutorizzato() {
      return true;
    }
  }

  record ErrMovimento(String errore) implements ResultMovimento {

    @Override
    public boolean movimentoAutorizzato() {
      return false;
    }
  }

}
