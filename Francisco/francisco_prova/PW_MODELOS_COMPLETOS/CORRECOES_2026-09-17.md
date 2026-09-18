# Correções — 17/09/2026

## WebSocket / SimpMessagingTemplate
Corrigidas as chamadas `convertAndSend(...)` que podiam ficar ambíguas no Spring/JDK 21 quando o payload era criado diretamente com `Map.of(...)`.

Antes:
```java
messaging.convertAndSend("/topic/gestao", Map.of("action", a, "productId", p.getId()));
```

Depois:
```java
messaging.convertAndSend("/topic/gestao", (Object) Map.of("action", a, "productId", p.getId()));
```

A mesma correção foi aplicada aos eventos WebSocket dos projetos Rede Social e Loja Online.
