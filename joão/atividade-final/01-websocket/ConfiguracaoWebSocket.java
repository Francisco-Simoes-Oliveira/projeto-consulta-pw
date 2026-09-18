package com.financas.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Liga o WebSocket no projeto.
 *
 * É a única classe de configuração necessária: ela abre o endereço de conexão (/ws)
 * e cria o "quadro de avisos" (/topic) onde o backend publica as mensagens.
 */
@Configuration
@EnableWebSocketMessageBroker
public class ConfiguracaoWebSocket implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Canais que o navegador pode escutar. Ex.: /topic/carteira/1
        registry.enableSimpleBroker("/topic");

        // Prefixo de quando o navegador manda mensagem PARA o backend (@MessageMapping).
        // No nosso caso quase não é usado: quem cria transação é o POST REST normal.
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // em produção troque "*" pelo domínio do seu front (ex.: https://meuapp.vercel.app)
                .setAllowedOriginPatterns("*")
                // SockJS é o plano B para navegadores/redes que bloqueiam WebSocket
                .withSockJS();
    }
}
