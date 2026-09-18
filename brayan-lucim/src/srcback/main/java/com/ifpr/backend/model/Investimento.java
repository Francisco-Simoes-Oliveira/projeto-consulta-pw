package com.ifpr.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "investimento")
public class Investimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "valor inicial obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message="O valor do investimento inicial deve ser positivo")
    private Double valorInicial;

    @NotNull(message = "O prazo é obrigatório")
    @Min(value = 1, message="O prazo deve ser positivo")
    @Max(value = 600, message = "O prazo não pode ser maior que 600 meses")
    private Integer prazoMeses;

    @NotNull(message = "A taxa de juros é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message="A taxa deve ser positiva")
    @DecimalMax(value = "100", inclusive = true, message = "A taxa não pode ser maior que 100")
    private Double taxaJuros;

    @NotNull(message = "o tipo de redimento é obrigatório")
    private String tipoRendimento;

    private Double valorFinal;

    private LocalDateTime dataCalculo;
}
