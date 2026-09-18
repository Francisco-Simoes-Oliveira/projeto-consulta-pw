package com.financas.backend.ia;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Única classe que conversa com a API do Gemini.
 *
 * Quem usa IA no sistema chama "perguntar(prompt)" e recebe o texto de volta.
 * Se um dia você trocar o Gemini por outro provedor, só esta classe muda.
 */
@Component
public class GeminiClient {

    private static final String URL_BASE = "https://generativelanguage.googleapis.com/v1beta";

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.modelo:gemini-2.5-flash}")
    private String modelo;

    private final RestClient restClient;

    public GeminiClient() {
        // timeout: se a IA demorar demais, desiste em vez de travar a requisição do usuário
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(5000);  // 5 segundos para conectar
        fabrica.setReadTimeout(20000);    // 20 segundos esperando a resposta

        this.restClient = RestClient.builder()
                .baseUrl(URL_BASE)
                .requestFactory(fabrica)
                .build();
    }

    public String perguntar(String prompt) {
        Map<String, Object> corpo = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)))));

        Map<?, ?> resposta = restClient.post()
                .uri("/models/{modelo}:generateContent", modelo)
                // a chave vai no header, não na URL (URL aparece em log de proxy)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(corpo)
                .retrieve()
                .body(Map.class);

        return extrairTexto(resposta);
    }

    /**
     * A resposta vem assim:
     * { "candidates": [ { "content": { "parts": [ { "text": "..." } ] } } ] }
     */
    @SuppressWarnings("unchecked")
    private String extrairTexto(Map<?, ?> resposta) {
        if (resposta == null) {
            throw new IllegalStateException("Resposta vazia da IA");
        }

        List<Map<String, Object>> candidatos = (List<Map<String, Object>>) resposta.get("candidates");
        if (candidatos == null || candidatos.isEmpty()) {
            throw new IllegalStateException("A IA não retornou nenhuma resposta");
        }

        Map<String, Object> conteudo = (Map<String, Object>) candidatos.get(0).get("content");
        List<Map<String, Object>> partes = (List<Map<String, Object>>) conteudo.get("parts");

        return String.valueOf(partes.get(0).get("text")).trim();
    }
}
