package com.simulado2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double distancia;

    @Column(nullable = false)
    private Double tempo; // Em minutos

    @Column(nullable = false)
    private Double velocidadeMedia; // Em km/h

    @Column(nullable = false)
    private String classificacao; // Caminhada, Trote ou Corrida

    @Column(nullable = false)
    private LocalDateTime dataHora;
}
