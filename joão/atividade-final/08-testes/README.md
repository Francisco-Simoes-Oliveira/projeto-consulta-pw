# 08 — Testes automatizados

**Requisito 1.7.** Dois testes bem escolhidos já mostram que você sabe testar as duas camadas:

| Teste | O que prova | Arquivo |
|---|---|---|
| Unitário de service | A **regra de negócio** está certa, sem banco e sem servidor | [RelatorioServiceTest.java](RelatorioServiceTest.java) |
| Integração de controller | A **rota** responde o status e o JSON certos | [CarteiraControllerTest.java](CarteiraControllerTest.java) |

## Passo a passo

1. Dependências (a primeira já vem do Spring Initializr):

```xml
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

2. Os arquivos vão em `src/test/java/...` (espelhando o pacote da classe testada).
3. Rodar:

```bash
mvnw test
```

## Vocabulário rápido

- **@Mock / @MockitoBean**: um dublê. Em vez do repositório de verdade (que precisaria de banco),
  você diz "quando chamarem esse método, devolva isso aqui".
- **@InjectMocks**: cria o service já com os dublês dentro.
- **MockMvc**: simula uma requisição HTTP sem subir o Tomcat de verdade.
- **AAA**: todo teste tem três partes — *Arrange* (prepara), *Act* (executa), *Assert* (confere).

## Observação de versão

`@MockitoBean` é o nome novo (Spring Boot 3.4 em diante). Se o seu projeto for Spring Boot 3.3 ou
anterior, troque por `@MockBean` (`org.springframework.boot.test.mock.mockito.MockBean`).

## Se quiser ir além

- Teste de repositório com `@DataJpaTest` + banco H2 em memória.
- No front, `@testing-library/react` para testar um componente (`render`, `screen`, `fireEvent`).
