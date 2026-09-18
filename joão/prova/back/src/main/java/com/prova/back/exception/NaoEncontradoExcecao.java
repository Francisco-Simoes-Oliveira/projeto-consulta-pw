package com.prova.back.exception;

public class NaoEncontradoExcecao extends RuntimeException {

    public NaoEncontradoExcecao(String mensagem) {
        super(mensagem);
    }
}
