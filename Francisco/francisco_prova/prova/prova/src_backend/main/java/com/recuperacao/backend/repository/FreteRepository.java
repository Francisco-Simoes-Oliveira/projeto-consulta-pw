package com.recuperacao.backend.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.recuperacao.backend.model.Frete;

@Repository
public interface FreteRepository extends JpaRepository<Frete,Long>{

}
