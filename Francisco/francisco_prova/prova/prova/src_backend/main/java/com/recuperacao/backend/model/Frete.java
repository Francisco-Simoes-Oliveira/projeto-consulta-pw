package com.recuperacao.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.recuperacao.backend.enums.TipoEnvio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Entity
@Data
@Table(name = "simulados")
public class Frete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @Min(0)
    private double kg;
    @Positive
    @Min(1)
    @Max(5000)
    private double km;
    // aparentemente 0 não e positivo
    @Min(0)
    @Max(100)
    private double adicDeUrgencia;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipo;

    private double valorFrete;
    private LocalDateTime calculadoEm;

}
