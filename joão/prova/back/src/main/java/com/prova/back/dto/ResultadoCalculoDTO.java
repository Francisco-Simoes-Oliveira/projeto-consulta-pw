package com.prova.back.dto;

import com.prova.back.models.TipoRendimento;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultadoCalculoDTO {

    private Double valorInicial;
    private Integer prazoMeses;
    private Double taxaMensal;
    private TipoRendimento tipoRendimento;
    private Double valorFinal;
    private Double rendimento;
}
