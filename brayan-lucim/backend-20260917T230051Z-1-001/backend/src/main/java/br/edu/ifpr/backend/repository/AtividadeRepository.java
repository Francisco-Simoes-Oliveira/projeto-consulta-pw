package br.edu.ifpr.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpr.backend.model.Atividade;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    
}
