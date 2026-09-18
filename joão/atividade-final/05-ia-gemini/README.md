# 05 — IA (Gemini)

**Requisito 1.4.** A IA precisa fazer parte de uma funcionalidade real. Aqui ela faz duas coisas:

1. **Categoriza a transação sozinha**: o usuário digita "Uber para o trabalho" e o sistema já marca
   como `TRANSPORTE`.
2. **Escreve um resumo dos gastos** em português, a partir dos números do relatório (pasta 04).

## Pegando a chave (plano gratuito)

1. Entre em <https://aistudio.google.com/apikey> com a sua conta Google.
2. Clique em "Create API key" e copie a chave.
3. Guarde numa variável de ambiente — **nunca** no código:

```bash
set GEMINI_API_KEY=sua-chave-aqui
```

4. No `application.properties`:

```properties
gemini.api-key=${GEMINI_API_KEY}
gemini.modelo=gemini-2.5-flash
```

No deploy, cadastre `GEMINI_API_KEY` nas variáveis de ambiente do serviço (Render, Railway…).

## Passo a passo

1. Copie [GeminiClient.java](GeminiClient.java) (fala com a API) e
   [CategorizacaoService.java](CategorizacaoService.java) (monta o prompt e trata a resposta).
2. Copie [IaController.java](IaController.java).
3. No service de transação, se a categoria vier vazia, chame `categorizacaoService.sugerirCategoria(...)`.

Não precisa de dependência nova: o `RestClient` já vem no `spring-boot-starter-web`.

## Como a API funciona por baixo

```
POST https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
Header: x-goog-api-key: SUA_CHAVE
Body:   { "contents": [ { "parts": [ { "text": "seu prompt" } ] } ] }

Resposta: { "candidates": [ { "content": { "parts": [ { "text": "resposta" } ] } } ] }
```

## Cuidados (dão ponto na defesa)

- **A chave fica só no backend.** Se o React chamasse o Gemini direto, a chave apareceria no
  DevTools de qualquer usuário.
- **Nunca confie cegamente na resposta.** O `CategorizacaoService` confere se a categoria devolvida
  está na lista permitida; se não estiver, usa `OUTROS`. Isso evita tanto alucinação quanto
  *prompt injection* (usuário escrevendo "ignore as instruções…" na descrição).
- **Se a IA falhar, o sistema continua.** Categorizar é um extra: em caso de erro/timeout, salve a
  transação como `OUTROS` em vez de quebrar o cadastro.
- **Custa tempo:** a chamada leva de 1 a 3 segundos. Mostre um "pensando…" na tela.
