package com.prova.back.exception;

public class NegocioExcecao extends RuntimeException {

    public NegocioExcecao(String mensagem) {
        super(mensagem);
    }
}
