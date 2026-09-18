# Guia de estudo — Loja Online


## Estrutura comum

- React + Vite + Axios
- Spring Boot + Spring Security + JWT + BCrypt
- JPA/Hibernate + banco H2 em arquivo
- Bean Validation (`@NotBlank`, `@NotNull`, `@Min`, `@DecimalMin`, `@Size`)
- `GlobalExceptionHandler` para erros de validação e regras de negócio
- Swagger/OpenAPI
- WebSocket/STOMP disponível para eventos em tempo real
- Perfis `ADMIN` e `USER`
- `/api/system/time` para exemplo de data/hora vinda do backend
- `BaseService.js` como exemplo genérico de CRUD no frontend

### Fluxo de autenticação

1. React envia `POST /api/auth/login`.
2. Spring autentica com `AuthenticationManager` e BCrypt.
3. `JwtService` gera o token.
4. O frontend salva o token no `localStorage`.
5. O interceptor Axios envia `Authorization: Bearer TOKEN`.
6. `JwtAuthFilter` valida o token antes do controller.

### Perfis de teste

- ADMIN: `admin@teste.com` / `123456`
- USER: `usuario@teste.com` / `123456`


## Entidades

`Product`, `CustomerOrder` e `OrderItem`. O carrinho fica no `localStorage`; o pedido definitivo é validado e persistido pelo backend.

## Regra principal

`OrderService.checkout()` usa `@Transactional`: busca os produtos, valida ativo/estoque, baixa as quantidades e grava pedido + itens. O usuário não envia preço; o preço é lido do banco, evitando confiar no frontend. Quando ADMIN cancela um pedido, as quantidades são devolvidas ao estoque e o pedido cancelado não pode ser reaberto.

## Permissões

- USER: catálogo, carrinho, checkout, dashboard pessoal e próprios pedidos.
- ADMIN: tudo do USER + CRUD de produtos, todos os pedidos, alteração de status e usuários.

## Endpoints principais

- `GET /api/products`
- `GET /api/products/admin` (ADMIN)
- `POST/PUT/DELETE /api/products` (ADMIN)
- `GET /api/orders`
- `POST /api/orders`
- `PATCH /api/orders/{id}/status` (ADMIN)
- `GET /api/dashboard`
- `GET /api/users` (ADMIN)

## Pontos bons para adaptar na prova

O mesmo padrão de checkout serve para pedido de restaurante, reserva de ingresso, locação, encomenda, sistema de matrícula e qualquer fluxo com cabeçalho + vários itens.
