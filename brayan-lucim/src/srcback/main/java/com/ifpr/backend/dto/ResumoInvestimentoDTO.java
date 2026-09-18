package com.ifpr.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResumoInvestimentoDTO {
    LocalDateTime dataHora;

    LocalDateTime ultimaSimulacao;

    Integer quantidadeSimulacoes;

    double valorFinalMedio;
}
