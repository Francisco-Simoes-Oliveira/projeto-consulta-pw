package com.financas.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Deixa o Swagger com cara de documentação de verdade e habilita o botão "Authorize",
 * para conseguir testar as rotas protegidas por JWT direto na tela.
 */
@Configuration
public class ConfiguracaoSwagger {

    private static final String ESQUEMA_JWT = "bearerAuth";

    @Bean
    public OpenAPI documentacao() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Gestão Financeira Pessoal e Compartilhada")
                        .version("1.0.0")
                        .description("""
                                API do sistema de finanças: usuários, carteiras compartilhadas,
                                transações, relatórios, notificações em tempo real e recursos de IA.
                                """)
                        .contact(new Contact().name("João").email("seu-email@exemplo.com")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT))
                .components(new Components().addSecuritySchemes(ESQUEMA_JWT,
                        new SecurityScheme()
                                .name(ESQUEMA_JWT)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Cole aqui o token devolvido por POST /api/auth/login")));
    }
}
