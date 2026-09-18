# 01 — WebSocket (saldo da carteira ao vivo)

**Requisito 1.2.** Quando um membro lança uma transação, todos os outros que estão com a carteira
aberta veem o saldo mudar na hora, sem apertar F5.

## Como funciona (bem resumido)

```
Navegador do membro A                    Backend                     Navegador do membro B
  POST /api/transacao   ------------->  TransacaoService
                                        salva no banco
                                        messagingTemplate
                                          .convertAndSend(...)  --->  já estava inscrito em
                                                                      /topic/carteira/1
                                                                      e atualiza a tela
```

- `/ws` → endereço onde o navegador abre a conexão.
- `/topic/carteira/{id}` → "canal" de cada carteira. Quem está na carteira 1 só recebe as mensagens dela.
- `SimpMessagingTemplate` → a classe do Spring que empurra a mensagem para o canal.

## Passo a passo

1. Adicione a dependência `spring-boot-starter-websocket` no `pom.xml`.
2. Copie [ConfiguracaoWebSocket.java](ConfiguracaoWebSocket.java) para `config/`.
3. No seu service de transação, injete o `SimpMessagingTemplate` e envie o evento depois de salvar —
   veja [TransacaoService.java](TransacaoService.java).
4. Crie o DTO do evento: [SaldoAtualizadoDTO.java](SaldoAtualizadoDTO.java).
5. No frontend, instale as bibliotecas do cliente e use o hook de [ClienteReact.js](ClienteReact.js):

```bash
npm install @stomp/stompjs sockjs-client
```

## Cuidados

- **Segurança:** se você usar Spring Security (pasta 03), libere o endpoint do handshake:
  `.requestMatchers("/ws/**").permitAll()`. O controle de quem vê o quê continua valendo no canal:
  só mande para `/topic/carteira/{id}` quem já é membro daquela carteira.
- **Nunca** mande a entidade JPA inteira no evento: mande um DTO pequeno (saldo, descrição, quem lançou).
- Se o front estiver em outra porta/domínio, o `setAllowedOriginPatterns` precisa aceitar essa origem.

## Quando usar WebSocket e quando usar SSE (pasta 02)

| | WebSocket | SSE |
|---|---|---|
| Direção | Os dois lados falam | Só o servidor fala |
| Complexidade | Maior (STOMP, SockJS) | Bem menor (é só um GET que não fecha) |
| Bom para | Chat, colaboração | Notificação, dashboard ao vivo |

Para a atividade, **um dos dois já cumpre o requisito**. Se a sua ideia é só "avisar o navegador",
o SSE é mais simples de explicar na apresentação.
