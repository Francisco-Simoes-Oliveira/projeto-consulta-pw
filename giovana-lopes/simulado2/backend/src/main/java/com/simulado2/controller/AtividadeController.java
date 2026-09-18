package com.simulado2.controller;

import com.simulado2.dto.AtividadeDTO;
import com.simulado2.model.Atividade;
import com.simulado2.service.AtividadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    // POST /atividades/calcular – recebe distância e tempo, retorna velocidade e classificação
    @PostMapping("/calcular")
    public ResponseEntity<AtividadeDTO.Response> calcular(@Valid @RequestBody AtividadeDTO.Request req) {
        return ResponseEntity.ok(atividadeService.calcular(req));
    }

    // POST /atividades – salva um registro
    @PostMapping
    public ResponseEntity<Atividade> salvar(@Valid @RequestBody AtividadeDTO.Request req) {
        return ResponseEntity.ok(atividadeService.salvar(req));
    }

    // GET /atividades – lista registros
    @GetMapping
    public ResponseEntity<List<Atividade>> listar() {
        return ResponseEntity.ok(atividadeService.listar());
    }

    // DELETE /atividades – apaga todos os registros
    @DeleteMapping
    public ResponseEntity<Void> apagarTodos() {
        atividadeService.apagarTodos();
        return ResponseEntity.noContent().build();
    }
}
