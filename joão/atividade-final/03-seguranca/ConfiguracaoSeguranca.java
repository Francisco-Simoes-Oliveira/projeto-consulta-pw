package com.financas.backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.financas.backend.security.FiltroJwt;

/**
 * Coração da segurança: quem pode entrar, como a senha é guardada,
 * quais sites podem chamar a API e quais cabeçalhos de proteção são enviados.
 */
@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

    @Autowired
    private FiltroJwt filtroJwt;

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configuracaoCors()))

                // API sem cookie de sessão: o token vai no header, então não existe CSRF aqui.
                // (Se você guardar o token em cookie, LIGUE o csrf de novo.)
                .csrf(csrf -> csrf.disable())

                // nada de sessão no servidor: cada requisição se identifica pelo token
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(rotas -> rotas
                        // rotas públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // todo o resto exige token válido
                        .anyRequest().authenticated())

                // cabeçalhos de segurança (A05 - configuração insegura)
                .headers(cabecalhos -> cabecalhos
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                        .frameOptions(frame -> frame.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)))

                // nosso filtro lê o token antes do filtro padrão de login
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Senha NUNCA é salva em texto puro. O BCrypt gera um hash com "sal" aleatório,
     * então duas pessoas com a mesma senha têm hashes diferentes.
     *
     * Ao cadastrar:  usuario.setSenha(passwordEncoder.encode(senhaDigitada));
     * Ao logar:      passwordEncoder.matches(senhaDigitada, usuario.getSenha());
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Só o seu front pode chamar a API. Evite "*" em produção.
     */
    @Bean
    public CorsConfigurationSource configuracaoCors() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://seu-app.vercel.app"));
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }
}
