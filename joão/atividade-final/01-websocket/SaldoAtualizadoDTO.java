package com.financas.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * O que viaja pelo WebSocket.
 *
 * Mande sempre um DTO enxuto, nunca a entidade JPA: a entidade pode ter senha,
 * lista de membros, relacionamentos preguiçosos (lazy) e vira um problemão no JSON.
 */
@Data
@AllArgsConstructor
public class SaldoAtualizadoDTO {

    private Long carteiraId;
    private BigDecimal saldoAtual;
    private String descricaoTransacao;
    private BigDecimal valorTransacao;
    private String nomeDeQuemLancou;
    private LocalDateTime dataHora;
}
