# Guia de estudo — Gestão Empresarial


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

`Product` guarda cadastro e saldo atual de estoque. `StockMovement` registra histórico de entrada, saída, ajuste e venda. `Sale` possui vários `SaleItem`. `FinancialEntry` guarda entradas e saídas.

## Regra principal

`SaleService.create()` usa `@Transactional`. Para cada item ele busca o produto, valida se está ativo, valida saldo, baixa o estoque via `ProductService.move()`, cria os itens da venda e depois lança automaticamente uma receita no financeiro. Se qualquer etapa falhar, a transação inteira é revertida.

## Permissões

- USER: dashboard, consulta de produtos, criação de venda e histórico das próprias vendas.
- ADMIN: tudo do USER + CRUD de produtos, movimentação manual de estoque, financeiro, relatórios e usuários.

## Endpoints principais

- `GET /api/dashboard`
- `GET /api/products`
- `POST/PUT/DELETE /api/products` (ADMIN)
- `POST /api/products/{id}/stock` (ADMIN)
- `GET /api/stock/movements` (ADMIN)
- `GET/POST /api/sales`
- `GET/POST/PUT/DELETE /api/finance` (ADMIN)
- `GET /api/users` e `PATCH /api/users/{id}/role` (ADMIN)

## Pontos bons para adaptar na prova

Troque `Product` por Livro/Produto/Serviço; troque `Sale` por Pedido/Reserva/Empréstimo; use a mesma estrutura de DTO → Repository → Service → Controller. A tela `ReportsPage.jsx` mostra CSV e impressão/PDF pelo navegador.
