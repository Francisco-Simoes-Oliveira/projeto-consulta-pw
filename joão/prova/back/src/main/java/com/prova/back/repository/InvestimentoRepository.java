package com.prova.back.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prova.back.models.Investimento;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    List<Investimento> findByDataCalculoBetweenAndPrazoMesesBetweenAndTaxaMensalBetweenOrderByDataCalculoDesc(
            LocalDateTime dataInicio, LocalDateTime dataFim,
            Integer prazoMinimo, Integer prazoMaximo,
            Double taxaMinima, Double taxaMaxima);

    @Query("select avg(i.valorFinal) from Investimento i")
    Double calcularValorFinalMedio();

    @Query("select max(i.dataCalculo) from Investimento i")
    LocalDateTime buscarDataUltimaSimulacao();
}
