package br.edu.ifpr.backend.model;

import java.time.LocalDateTime;

import br.edu.ifpr.backend.enums.Classificacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double distancia;
    private int tempo;
    private double velocidadeMedia;
    private LocalDateTime dataHora;
    
    @Enumerated(EnumType.STRING)
    private Classificacao classificacao;
}
