package com.prova.back.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prova.back.dto.DataHoraDTO;
import com.prova.back.dto.ResultadoCalculoDTO;
import com.prova.back.dto.ResumoDTO;
import com.prova.back.models.Investimento;
import com.prova.back.service.InvestimentoService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/investimento")
public class InvestimentoController {

    @Autowired
    private InvestimentoService investimentoService;

    @PostMapping("/calcular")
    public ResultadoCalculoDTO calcular(@RequestBody @Valid Investimento investimento) {
        return investimentoService.calcular(investimento);
    }

    @PostMapping
    public ResponseEntity<Investimento> create(@RequestBody @Valid Investimento investimento) {
        return new ResponseEntity<>(investimentoService.create(investimento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Investimento update(@PathVariable("id") Long id, @RequestBody @Valid Investimento investimento) {
        return investimentoService.update(id, investimento);
    }

    @GetMapping
    public List<Investimento> listAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Integer prazoMinimo,
            @RequestParam(required = false) Integer prazoMaximo,
            @RequestParam(required = false) Double taxaMinima,
            @RequestParam(required = false) Double taxaMaxima) {
        return investimentoService.listar(dataInicio, dataFim, prazoMinimo, prazoMaximo, taxaMinima, taxaMaxima);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        investimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        investimentoService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    public ResumoDTO resumo() {
        return investimentoService.resumo();
    }

    @GetMapping("/data-hora")
    public DataHoraDTO dataHora() {
        return investimentoService.dataHoraAtual();
    }
}
