# 02 — Server-Sent Events (notificações ao vivo)

**Requisito 1.2.** Mesma ideia da pasta 01, mas muito mais simples: o navegador abre **um GET que
nunca termina** e o servidor vai escrevendo mensagens nessa conexão.

## Como funciona

```
Navegador:  GET /api/notificacoes/stream?carteiraId=1     (fica aberto)
Backend:    guarda esse "canudo" (SseEmitter) numa lista
            quando alguém lança transação -> escreve no canudo
Navegador:  onmessage dispara e a tela atualiza
```

Não precisa de nenhuma dependência nova: `SseEmitter` já vem no Spring Web, e `EventSource` já vem
no navegador (nada de npm install).

## Passo a passo

1. Copie [NotificacaoSseService.java](NotificacaoSseService.java) para `service/` — é ele que guarda as
   conexões abertas e envia as mensagens.
2. Copie [NotificacaoSseController.java](NotificacaoSseController.java) para `controller/` — é a rota que
   o navegador abre.
3. No service de transação, chame `notificacaoSseService.enviarParaCarteira(...)` depois de salvar
   (igual ao exemplo da pasta 01, trocando o `SimpMessagingTemplate` por este service).
4. No front, use [ClienteReact.js](ClienteReact.js).

## Detalhes que costumam derrubar o SSE

- **Timeout:** a conexão morre depois de um tempo. O `EventSource` reconecta sozinho, mas o backend
  precisa remover o emitter morto da lista (é o que o `onCompletion`/`onTimeout`/`onError` fazem).
- **`IOException` ao enviar:** significa que aquele navegador fechou. Remova o emitter e siga.
- **Token JWT:** o `EventSource` do navegador **não** deixa mandar header `Authorization`. As duas
  saídas mais fáceis são: mandar o token como parâmetro na URL (e validar no backend) ou liberar essa
  rota e mandar só dados não sensíveis. Escolha uma e explique na apresentação.
- **Proxy/deploy:** em serviços como o Render, desative buffer/compressão para essa rota, senão a
  mensagem só chega quando a conexão fecha.

## SSE ou WebSocket?

Se o servidor só precisa **avisar** o navegador (notificação, saldo, dashboard), SSE resolve com
metade do código. WebSocket vale quando os dois lados conversam (chat, edição colaborativa).
