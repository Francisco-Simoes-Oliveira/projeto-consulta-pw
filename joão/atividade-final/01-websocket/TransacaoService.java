package com.financas.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.financas.backend.dto.SaldoAtualizadoDTO;
import com.financas.backend.model.Transacao;
import com.financas.backend.repository.TransacaoRepository;

/**
 * Service normal de transação, com UMA linha a mais: depois de salvar,
 * avisa quem está com a carteira aberta.
 *
 * Repare que o controller não muda nada: ele continua só chamando o service.
 */
@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    /** Classe do Spring que publica mensagens nos canais do WebSocket. */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Transacao criar(Transacao transacao) {
        transacao.setData(LocalDateTime.now());
        Transacao salva = transacaoRepository.save(transacao);

        avisarMembrosDaCarteira(salva);

        return salva;
    }

    private void avisarMembrosDaCarteira(Transacao transacao) {
        Long carteiraId = transacao.getCarteira().getId();

        // soma RECEITA - DESPESA no banco (veja a query na pasta 04-relatorios)
        BigDecimal saldo = transacaoRepository.calcularSaldo(carteiraId);

        SaldoAtualizadoDTO evento = new SaldoAtualizadoDTO(
                carteiraId,
                saldo,
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getCarteira().getDono().getNome(),
                transacao.getData());

        // todo navegador inscrito em /topic/carteira/5 recebe isso na hora
        messagingTemplate.convertAndSend("/topic/carteira/" + carteiraId, evento);
    }
}
