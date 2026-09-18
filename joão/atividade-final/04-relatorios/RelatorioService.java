package com.financas.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financas.backend.dto.ResumoCategoriaDTO;
import com.financas.backend.model.Transacao;
import com.financas.backend.repository.TransacaoRepository;

/**
 * Dois relatórios: resumo por categoria (JSON) e extrato em CSV.
 *
 * Toda soma é feita no banco (sum/group by). O Java só formata o resultado.
 */
@Service
public class RelatorioService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private TransacaoRepository transacaoRepository;

    /** Quanto foi gasto em cada categoria no mês, com o percentual de cada uma. */
    public List<ResumoCategoriaDTO> resumoPorCategoria(Long carteiraId, int ano, int mes) {
        YearMonth mesDesejado = YearMonth.of(ano, mes);
        LocalDateTime inicio = mesDesejado.atDay(1).atStartOfDay();
        LocalDateTime fim = mesDesejado.atEndOfMonth().atTime(23, 59, 59);

        List<ResumoCategoriaDTO> resumo = transacaoRepository.somarDespesasPorCategoria(carteiraId, inicio, fim);

        BigDecimal totalGeral = resumo.stream()
                .map(ResumoCategoriaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (ResumoCategoriaDTO linha : resumo) {
            linha.setPercentual(calcularPercentual(linha.getTotal(), totalGeral));
        }

        return resumo;
    }

    /** Extrato do mês em CSV, pronto para abrir no Excel. */
    public byte[] extratoCsv(Long carteiraId, int ano, int mes) {
        YearMonth mesDesejado = YearMonth.of(ano, mes);
        List<Transacao> transacoes = transacaoRepository.buscarDoPeriodo(
                carteiraId,
                mesDesejado.atDay(1).atStartOfDay(),
                mesDesejado.atEndOfMonth().atTime(23, 59, 59));

        StringBuilder csv = new StringBuilder();
        csv.append('﻿'); // BOM: faz o Excel entender os acentos
        csv.append("Data;Descrição;Categoria;Tipo;Valor\n");

        for (Transacao transacao : transacoes) {
            csv.append(transacao.getData().format(FORMATO_DATA)).append(';')
                    .append(limpar(transacao.getDescricao())).append(';')
                    .append(limpar(transacao.getCategoria())).append(';')
                    .append(transacao.getTipo()).append(';')
                    .append(transacao.getValor().toString().replace('.', ',')).append('\n');
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private BigDecimal calcularPercentual(BigDecimal valor, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valor.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP);
    }

    /** Tira ; e quebra de linha, que bagunçariam as colunas do CSV. */
    private String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(";", ",").replace("\n", " ").trim();
    }
}

/*
 * ---------------------------------------------------------------------------
 * Consultas no TransacaoRepository:
 * ---------------------------------------------------------------------------
 *
 * @Query("""
 *         select new com.financas.backend.dto.ResumoCategoriaDTO(t.categoria, sum(t.valor))
 *         from Transacao t
 *         where t.carteira.id = :carteiraId
 *           and t.tipo = com.financas.backend.model.TipoTransacao.DESPESA
 *           and t.data between :inicio and :fim
 *         group by t.categoria
 *         order by sum(t.valor) desc
 *         """)
 * List<ResumoCategoriaDTO> somarDespesasPorCategoria(@Param("carteiraId") Long carteiraId,
 *         @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
 *
 * @Query("""
 *         select t from Transacao t
 *         where t.carteira.id = :carteiraId and t.data between :inicio and :fim
 *         order by t.data
 *         """)
 * List<Transacao> buscarDoPeriodo(@Param("carteiraId") Long carteiraId,
 *         @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
 *
 * // usada no exemplo de WebSocket (pasta 01): receitas - despesas
 * @Query("""
 *         select coalesce(sum(case when t.tipo = com.financas.backend.model.TipoTransacao.RECEITA
 *                                  then t.valor else -t.valor end), 0)
 *         from Transacao t where t.carteira.id = :carteiraId
 *         """)
 * BigDecimal calcularSaldo(@Param("carteiraId") Long carteiraId);
 *
 * ---------------------------------------------------------------------------
 * O DTO precisa ter um construtor com (String, BigDecimal) para o "select new" funcionar:
 * ---------------------------------------------------------------------------
 *
 * @Data
 * public class ResumoCategoriaDTO {
 *     private String categoria;
 *     private BigDecimal total;
 *     private BigDecimal percentual;
 *
 *     public ResumoCategoriaDTO(String categoria, BigDecimal total) {
 *         this.categoria = categoria;
 *         this.total = total;
 *     }
 * }
 */
