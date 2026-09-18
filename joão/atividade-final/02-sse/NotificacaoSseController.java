package com.financas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.financas.backend.service.NotificacaoSseService;

/**
 * Uma rota só: o navegador chama e a conexão fica aberta recebendo eventos.
 *
 * O segredo está no produces = text/event-stream — é isso que diz ao navegador
 * "essa resposta vai chegando aos poucos".
 */
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoSseController {

    @Autowired
    private NotificacaoSseService notificacaoSseService;

    // GET http://localhost:8080/api/notificacoes/stream?carteiraId=1
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam("carteiraId") Long carteiraId) {
        return notificacaoSseService.conectar(carteiraId);
    }
}
