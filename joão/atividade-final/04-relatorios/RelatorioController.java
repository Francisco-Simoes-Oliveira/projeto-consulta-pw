package com.financas.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.backend.dto.ResumoCategoriaDTO;
import com.financas.backend.service.RelatorioService;

/**
 * Duas rotas: uma devolve JSON (para a tela) e a outra devolve um arquivo (download).
 */
@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // GET /api/relatorio/resumo?carteiraId=1&ano=2026&mes=11
    @GetMapping("/resumo")
    public List<ResumoCategoriaDTO> resumo(
            @RequestParam Long carteiraId,
            @RequestParam int ano,
            @RequestParam int mes) {
        return relatorioService.resumoPorCategoria(carteiraId, ano, mes);
    }

    // GET /api/relatorio/extrato.csv?carteiraId=1&ano=2026&mes=11
    @GetMapping("/extrato.csv")
    public ResponseEntity<byte[]> extratoCsv(
            @RequestParam Long carteiraId,
            @RequestParam int ano,
            @RequestParam int mes) {

        byte[] arquivo = relatorioService.extratoCsv(carteiraId, ano, mes);
        String nomeArquivo = String.format("extrato-%d-%02d.csv", ano, mes);

        return ResponseEntity.ok()
                // é isso que faz o navegador baixar em vez de exibir
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(arquivo);
    }
}

/*
 * No React, o download fica assim (o axios precisa do responseType blob):
 *
 * const baixarExtrato = async () => {
 *     const resposta = await api.get("relatorio/extrato.csv", {
 *         params: { carteiraId, ano, mes },
 *         responseType: "blob",
 *     });
 *     const url = URL.createObjectURL(resposta.data);
 *     const link = document.createElement("a");
 *     link.href = url;
 *     link.download = "extrato.csv";
 *     link.click();
 *     URL.revokeObjectURL(url);
 * };
 */
