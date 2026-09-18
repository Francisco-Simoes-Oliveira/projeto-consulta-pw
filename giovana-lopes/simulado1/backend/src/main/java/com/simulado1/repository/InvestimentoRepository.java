package com.simulado1.repository;

import com.simulado1.model.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    List<Investimento> findByDataCalculo(LocalDate dataCalculo);

    @Query("SELECT i FROM Investimento i WHERE i.prazoMeses = :termoNum OR i.juroMensal = :termoDouble")
    List<Investimento> filtrarPorPrazoOuJuro(@Param("termoNum") Integer termoNum, 
                                            @Param("termoDouble") Double termoDouble);
}
