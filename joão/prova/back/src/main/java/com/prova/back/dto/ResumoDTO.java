package com.prova.back.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumoDTO {

    private Long quantidadeSimulacoes;
    private Double valorFinalMedio;
    private LocalDateTime dataUltimaSimulacao;
}
