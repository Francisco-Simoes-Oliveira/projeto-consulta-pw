package com.simulado2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

public class AtividadeDTO {

    @Data
    public static class Request {
        @NotNull(message = "A distância percorrida é obrigatória.")
        @Positive(message = "A distância percorrida deve ser maior que zero.")
        private Double distancia;

        @NotNull(message = "O tempo gasto é obrigatório.")
        @Positive(message = "O tempo gasto deve ser maior que zero.")
        private Double tempo; // Em minutos
    }

    @Data
    public static class Response {
        private Double distancia;
        private Double tempo;
        private Double velocidadeMedia;
        private String classificacao;
    }
}
