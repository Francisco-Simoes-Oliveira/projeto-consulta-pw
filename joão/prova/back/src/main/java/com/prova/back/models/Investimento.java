package com.prova.back.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name = "investimentos")
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Informe o valor inicial")
    @Positive(message = "O valor inicial precisa ser maior que zero")
    @Column(nullable = false)
    private Double valorInicial;

    @NotNull(message = "Informe o prazo em meses")
    @Min(value = 1, message = "O prazo precisa ser de no mínimo 1 mês")
    @Max(value = 600, message = "O prazo precisa ser de no máximo 600 meses")
    @Column(nullable = false)
    private Integer prazoMeses;

    @NotNull(message = "Informe a taxa mensal")
    @DecimalMin(value = "0.0", message = "A taxa mensal não pode ser negativa")
    @DecimalMax(value = "100.0", message = "A taxa mensal precisa ser menor ou igual a 100")
    @Column(nullable = false)
    private Double taxaMensal;

    @NotNull(message = "Informe o tipo de rendimento")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRendimento tipoRendimento;

    @Column(nullable = false)
    private Double valorFinal;

    @Column(nullable = false)
    private LocalDateTime dataCalculo;
}
