package com.simulado1.controller;

import com.simulado1.dto.InvestimentoDTO;
import com.simulado1.model.Investimento;
import com.simulado1.service.InvestimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    @Autowired
    private InvestimentoService investimentoService;

    @PostMapping("/calcular")
    public ResponseEntity<InvestimentoDTO.Response> calcular(@Valid @RequestBody InvestimentoDTO.Request req) {
        return ResponseEntity.ok(investimentoService.calcular(req));
    }

    @PostMapping
    public ResponseEntity<Investimento> salvar(@Valid @RequestBody InvestimentoDTO.Request req) {
        return ResponseEntity.ok(investimentoService.salvar(req));
    }

    @GetMapping
    public ResponseEntity<List<Investimento>> listar() {
        return ResponseEntity.ok(investimentoService.listarTodos());
    }

    @GetMapping("/filtrar/data")
    public ResponseEntity<List<Investimento>> filtrarPorData(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(investimentoService.filtrarPorData(data));
    }

    @GetMapping("/filtrar/termo")
    public ResponseEntity<List<Investimento>> filtrarPorPrazoOuJuro(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(investimentoService.filtrarPorPrazoOuJuro(termo));
    }

    @DeleteMapping
    public ResponseEntity<Void> limparTabela() {
        investimentoService.limparTodos();
        return ResponseEntity.noContent().build();
    }
}
