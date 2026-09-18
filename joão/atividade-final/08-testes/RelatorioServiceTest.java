package com.financas.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.financas.backend.dto.ResumoCategoriaDTO;
import com.financas.backend.repository.TransacaoRepository;

/**
 * Teste UNITÁRIO: sem banco, sem Spring, sem servidor. Roda em milissegundos.
 *
 * O repositório é substituído por um dublê (@Mock): nós dizemos o que ele devolve
 * e conferimos se o service faz a conta certa em cima disso.
 */
@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Calcula o percentual de cada categoria sobre o total do mês")
    void deveCalcularPercentualPorCategoria() {
        // Arrange: o "banco" devolve 300 de alimentação e 100 de transporte (total 400)
        List<ResumoCategoriaDTO> retornoDoBanco = List.of(
                new ResumoCategoriaDTO("ALIMENTACAO", new BigDecimal("300.00")),
                new ResumoCategoriaDTO("TRANSPORTE", new BigDecimal("100.00")));

        when(transacaoRepository.somarDespesasPorCategoria(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(retornoDoBanco);

        // Act
        List<ResumoCategoriaDTO> resumo = relatorioService.resumoPorCategoria(1L, 2026, 11);

        // Assert: 300/400 = 75% e 100/400 = 25%
        assertEquals(new BigDecimal("75.00"), resumo.get(0).getPercentual());
        assertEquals(new BigDecimal("25.00"), resumo.get(1).getPercentual());
    }

    @Test
    @DisplayName("Não quebra quando o mês não tem despesas")
    void deveDevolverListaVaziaQuandoNaoHaDespesas() {
        when(transacaoRepository.somarDespesasPorCategoria(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<ResumoCategoriaDTO> resumo = relatorioService.resumoPorCategoria(1L, 2026, 11);

        assertEquals(0, resumo.size());
    }
}
