# Material de consulta — Atividade Final (Gestão Financeira)

Exemplos curtos e comentados de cada requisito obrigatório da atividade final.
Cada pasta é independente: tem um `README.md` explicando o passo a passo e os arquivos Java prontos
para copiar e adaptar.

Quase tudo é Java (Spring Boot). Só três coisas não podem ser Java, porque rodam no navegador:
o cliente do WebSocket, o cliente do SSE e o PWA (manifest + service worker). Esses arquivos estão
marcados como `Cliente...js`.

> **Esta pasta é material de consulta, não um projeto que compila.** As classes citam entidades
> (`Usuario`, `Carteira`, `Transacao`) e repositórios que existem no *seu* projeto. Copie o arquivo
> para o seu pacote, ajuste o `package`/imports e o código roda.

## Índice

| Pasta | Requisito da atividade | O que tem dentro |
|---|---|---|
| [01-websocket](01-websocket) | 1.2 Tempo real | Saldo da carteira atualizando ao vivo quando outro membro lança transação |
| [02-sse](02-sse) | 1.2 Tempo real | Notificações via Server-Sent Events (mais simples que WebSocket) |
| [03-seguranca](03-seguranca) | 1.6 Segurança (OWASP) | Login com JWT, controle de acesso, rate limit, headers, SQL injection, segredos |
| [04-relatorios](04-relatorios) | 1.3 Relatórios | Resumo mensal por categoria + exportação em CSV |
| [05-ia-gemini](05-ia-gemini) | 1.4 IA | Categorização automática de transação e resumo dos gastos com a API do Gemini |
| [06-pwa](06-pwa) | 1.5 PWA | manifest.json, service worker com cache offline e registro no React |
| [07-swagger](07-swagger) | 1.9 Documentação | springdoc-openapi, `/swagger-ui.html` e anotações nos controllers |
| [08-testes](08-testes) | 1.7 Testes | Teste unitário de service (JUnit + Mockito) e de controller (MockMvc) |
| [09-deploy](09-deploy) | 1.8 Implantação | Dockerfile, docker-compose e passos de deploy (Render + Vercel) |

## Modelo usado nos exemplos

Todos os trechos assumem estas três entidades, para os exemplos combinarem entre si:

```java
Usuario   -> id, nome, email, senha (hash), ativo
Carteira  -> id, nome, dono (Usuario), membros (List<Usuario>)
Transacao -> id, carteira, descricao, valor, tipo (RECEITA | DESPESA), categoria, data
```

Pacote base dos exemplos: `com.financas.backend`.

## Dependências do `pom.xml`

Junte no seu `pom.xml` as dependências do que for usar:

```xml
<!-- Base (já vem do Spring Initializr: web, data-jpa, validation, lombok, driver do banco) -->

<!-- 01 - WebSocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

<!-- 03 - Segurança -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- 07 - Swagger / OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>

<!-- 08 - Testes -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

O **SSE (02)** e os **relatórios (04)** não precisam de dependência nova, e a **IA (05)** usa o
`RestClient`, que já vem no `spring-boot-starter-web`.

## Ordem sugerida para montar o projeto

1. Segurança primeiro (03): sem login, carteira compartilhada não faz sentido.
2. CRUD de carteiras e transações (você já tem da Parte 2).
3. Relatórios (04) — é só consulta em cima do que já existe.
4. Tempo real (01 ou 02) — dispare o evento de dentro do service de transação.
5. IA (05), Swagger (07) e testes (08).
6. PWA (06) e deploy (09) por último, quando o sistema já estiver funcionando.
