package com.simulado1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

public class InvestimentoDTO {

    @Data
    public static class Request {
        @NotNull(message = "O valor inicial é obrigatório.")
        @Positive(message = "O valor inicial deve ser maior que zero.")
        private Double valorInicial;

        @NotNull(message = "O prazo em meses é obrigatório.")
        @Positive(message = "O prazo deve ser de no mínimo 1 mês.")
        private Integer prazoMeses;

        @NotNull(message = "A taxa de juros mensal é obrigatória.")
        @Positive(message = "A taxa de juros deve ser maior que zero.")
        private Double juroMensal;
    }

    @Data
    public static class Response {
        private Double valorInicial;
        private Integer prazoMeses;
        private Double juroMensal;
        private Double valorFinal;
        private String mensagem;
    }
}
