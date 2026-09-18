package com.financas.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Guarda as conexões SSE abertas e envia mensagens para elas.
 *
 * A estrutura é só um mapa: carteiraId -> lista de navegadores conectados naquela carteira.
 * CopyOnWriteArrayList e ConcurrentHashMap são usados porque várias requisições mexem
 * na lista ao mesmo tempo (uma lista comum daria erro).
 */
@Service
public class NotificacaoSseService {

    /** 30 minutos. Depois disso o navegador reconecta sozinho. */
    private static final long TEMPO_LIMITE = 30 * 60 * 1000L;

    private final Map<Long, List<SseEmitter>> conexoesPorCarteira = new ConcurrentHashMap<>();

    /** Chamado pelo controller quando um navegador abre a tela da carteira. */
    public SseEmitter conectar(Long carteiraId) {
        SseEmitter emitter = new SseEmitter(TEMPO_LIMITE);

        List<SseEmitter> conexoes = conexoesPorCarteira
                .computeIfAbsent(carteiraId, chave -> new CopyOnWriteArrayList<>());
        conexoes.add(emitter);

        // sempre que a conexão acabar (fechou a aba, deu timeout, deu erro), tira da lista
        emitter.onCompletion(() -> conexoes.remove(emitter));
        emitter.onTimeout(() -> conexoes.remove(emitter));
        emitter.onError(erro -> conexoes.remove(emitter));

        enviar(emitter, "conectado", "Conexão aberta com a carteira " + carteiraId);

        return emitter;
    }

    /** Chamado pelo service de transação depois de salvar. */
    public void enviarParaCarteira(Long carteiraId, Object dados) {
        List<SseEmitter> conexoes = conexoesPorCarteira.get(carteiraId);

        if (conexoes == null) {
            return; // ninguém está com essa carteira aberta
        }

        for (SseEmitter emitter : conexoes) {
            enviar(emitter, "transacao", dados);
        }
    }

    private void enviar(SseEmitter emitter, String nomeDoEvento, Object dados) {
        try {
            emitter.send(SseEmitter.event().name(nomeDoEvento).data(dados));
        } catch (IOException erro) {
            // o navegador do outro lado sumiu: encerra a conexão (o onCompletion remove da lista)
            emitter.complete();
        }
    }
}
