package com.financas.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Cria e valida o token JWT.
 *
 * O token é só um texto assinado com a nossa chave secreta. Ele diz "quem é o usuário"
 * e "até quando vale". Como está assinado, ninguém consegue alterar sem a chave.
 *
 * ATENÇÃO: o conteúdo do token é legível por qualquer um (não é criptografado).
 * Nunca coloque senha ou dado sigiloso dentro dele.
 */
@Service
public class JwtService {

    /** Vem de variável de ambiente. Mínimo de 32 caracteres para o algoritmo HS256. */
    @Value("${jwt.secret}")
    private String segredo;

    /** 1 hora, por padrão. */
    @Value("${jwt.expiracao-ms:3600000}")
    private long expiracaoMs;

    public String gerarToken(String email, String nome) {
        Date agora = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("nome", nome)
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + expiracaoMs))
                .signWith(chave())
                .compact();
    }

    /**
     * Devolve o e-mail de dentro do token.
     * Se o token estiver vencido, adulterado ou com outra assinatura, lança JwtException.
     */
    public String extrairEmail(String token) {
        Claims dados = Jwts.parser()
                .verifyWith(chave())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return dados.getSubject();
    }

    private SecretKey chave() {
        return Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
    }
}
