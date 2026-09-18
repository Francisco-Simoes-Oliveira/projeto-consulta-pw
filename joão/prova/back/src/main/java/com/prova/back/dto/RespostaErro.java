package com.prova.back.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespostaErro {

    private int status;
    private String titulo;
    private String mensagem;
    private List<String> erros;
}
