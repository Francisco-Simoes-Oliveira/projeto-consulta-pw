package com.financas.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.backend.dto.ResumoCategoriaDTO;
import com.financas.backend.ia.CategorizacaoService;
import com.financas.backend.service.RelatorioService;

/**
 * Rotas que usam IA.
 *
 * Repare que o controller continua sem regra nenhuma: ele só chama os services.
 */
@RestController
@RequestMapping("/api/ia")
public class IaController {

    @Autowired
    private CategorizacaoService categorizacaoService;

    @Autowired
    private RelatorioService relatorioService;

    // POST /api/ia/categoria?descricao=Uber para o trabalho
    @PostMapping("/categoria")
    public Map<String, String> sugerirCategoria(@RequestParam String descricao) {
        return Map.of("categoria", categorizacaoService.sugerirCategoria(descricao));
    }

    // GET /api/ia/analise?carteiraId=1&ano=2026&mes=11
    @GetMapping("/analise")
    public Map<String, String> analisar(
            @RequestParam Long carteiraId,
            @RequestParam int ano,
            @RequestParam int mes) {

        List<ResumoCategoriaDTO> resumo = relatorioService.resumoPorCategoria(carteiraId, ano, mes);
        return Map.of("analise", categorizacaoService.analisarGastos(resumo));
    }
}
