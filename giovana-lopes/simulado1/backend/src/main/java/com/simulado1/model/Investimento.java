package com.simulado1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "investimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorInicial;

    @Column(nullable = false)
    private Integer prazoMeses;

    @Column(nullable = false)
    private Double juroMensal;

    @Column(nullable = false)
    private Double valorFinal;

    @Column(nullable = false)
    private LocalDate dataCalculo;
}
