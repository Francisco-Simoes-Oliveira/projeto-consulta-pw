package com.financas.backend.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Limita as tentativas de login por IP (OWASP A07).
 *
 * Sem isso, alguém roda um script testando milhares de senhas. Aqui são no máximo
 * 5 tentativas por minuto; a sexta recebe 429 (Too Many Requests).
 *
 * É uma versão simples, em memória, ótima para explicar na apresentação.
 * Em produção com várias instâncias, o controle iria para o Redis ou para uma
 * biblioteca como o Bucket4j.
 */
@Component
public class FiltroRateLimit extends OncePerRequestFilter {

    private static final String ROTA_PROTEGIDA = "/api/auth/login";
    private static final int MAXIMO_TENTATIVAS = 5;
    private static final long JANELA_SEGUNDOS = 60;

    private final Map<String, Tentativas> tentativasPorIp = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest requisicao) {
        // só vale para o POST do login; o resto da API passa direto
        return !(ROTA_PROTEGIDA.equals(requisicao.getRequestURI()) && "POST".equals(requisicao.getMethod()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta,
            FilterChain corrente) throws ServletException, IOException {

        String ip = requisicao.getRemoteAddr();
        long agora = Instant.now().getEpochSecond();

        Tentativas tentativas = tentativasPorIp.compute(ip, (chave, atual) -> {
            if (atual == null || agora - atual.inicioDaJanela >= JANELA_SEGUNDOS) {
                return new Tentativas(agora); // janela nova
            }
            atual.contador.incrementAndGet();
            return atual;
        });

        if (tentativas.contador.get() > MAXIMO_TENTATIVAS) {
            resposta.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            resposta.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resposta.setCharacterEncoding("UTF-8");
            resposta.getWriter().write(
                    "{\"status\":429,\"mensagem\":\"Muitas tentativas de login. Tente novamente em 1 minuto.\"}");
            return;
        }

        corrente.doFilter(requisicao, resposta);
    }

    /** Contador de tentativas de um IP dentro da janela de tempo. */
    private static class Tentativas {

        private final long inicioDaJanela;
        private final AtomicInteger contador = new AtomicInteger(1);

        private Tentativas(long inicioDaJanela) {
            this.inicioDaJanela = inicioDaJanela;
        }
    }
}
