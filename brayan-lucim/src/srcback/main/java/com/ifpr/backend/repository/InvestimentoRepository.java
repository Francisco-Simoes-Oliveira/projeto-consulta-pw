package com.ifpr.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpr.backend.model.Investimento;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    
    List<Investimento> findByDataCalculoBetween(
        LocalDateTime dataInicial,
        LocalDateTime dataFinal
    );

    List<Investimento> findByPrazoMesesBetween(
        Integer prazoInicial,
        Integer prazoFinal
    );
}
