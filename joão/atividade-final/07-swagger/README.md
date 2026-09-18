# 07 — Swagger / OpenAPI

**Requisito 1.9.** Documentação da API acessível e atualizada.

A boa notícia: o springdoc lê os seus controllers sozinho. Basta a dependência e a documentação já
existe. As anotações são para deixá-la bonita e explicada.

## Passo a passo

1. Dependência no `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>
```

2. Suba o backend e acesse:

| Endereço | O que é |
|---|---|
| <http://localhost:8080/swagger-ui.html> | A tela do Swagger |
| <http://localhost:8080/v3/api-docs> | O JSON do OpenAPI (é o que você entrega/versiona) |

3. Copie [ConfiguracaoSwagger.java](ConfiguracaoSwagger.java) para configurar título, versão e o
   botão **Authorize** (para testar rotas com JWT).
4. Anote os controllers como em [ExemploControllerDocumentado.java](ExemploControllerDocumentado.java).

## Se você usa Spring Security (pasta 03)

Libere as rotas do Swagger, senão a tela abre vazia ou dá 401:

```java
.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
```

Para testar rota protegida: faça `POST /api/auth/login` pelo próprio Swagger, copie o token da
resposta, clique em **Authorize**, cole o token e execute as outras rotas normalmente.

## Dica para o deploy

Em produção dá para expor só o JSON e esconder a tela:

```properties
springdoc.swagger-ui.enabled=false
```

Mas, como o professor vai avaliar a documentação, **deixe a tela ligada** e coloque o link dela no
README (ex.: `https://sua-api.onrender.com/swagger-ui.html`).
