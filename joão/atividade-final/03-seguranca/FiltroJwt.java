package com.financas.backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Roda em TODA requisição, antes de chegar no controller.
 *
 * Lê o header "Authorization: Bearer <token>", valida e, se estiver ok,
 * marca o usuário como autenticado. Se não tiver token, segue em frente:
 * quem barra a requisição é a regra do ConfiguracaoSeguranca.
 */
@Component
public class FiltroJwt extends OncePerRequestFilter {

    private static final String PREFIXO = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta,
            FilterChain corrente) throws ServletException, IOException {

        String cabecalho = requisicao.getHeader("Authorization");

        if (cabecalho != null && cabecalho.startsWith(PREFIXO)) {
            String token = cabecalho.substring(PREFIXO.length());

            try {
                String email = jwtService.extrairEmail(token);

                UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(new SimpleGrantedAuthority("ROLE_USUARIO")));

                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            } catch (JwtException erro) {
                // token inválido ou vencido: segue sem autenticar (vira 401 lá na frente)
                SecurityContextHolder.clearContext();
            }
        }

        corrente.doFilter(requisicao, resposta);
    }
}
