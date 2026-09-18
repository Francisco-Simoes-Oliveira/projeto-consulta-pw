# Guia de estudo — Rede Social


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

`Post`, `PostLike` e `Comment`. O autor é identificado pelo e-mail presente no JWT/autenticação; o frontend não escolhe quem é o autor.

## Regras principais

- O feed mostra somente posts não ocultos.
- Curtir é um toggle: cria a curtida se não existir e remove se existir.
- Um USER só pode excluir o próprio post.
- ADMIN pode ver todos os posts, ocultar/reexibir e excluir qualquer publicação.
- Eventos são enviados em `/topic/social` para atualizar o feed em outras abas.

## Endpoints principais

- `GET /api/posts`
- `GET /api/posts/mine`
- `POST /api/posts`
- `POST /api/posts/{id}/like`
- `GET/POST /api/posts/{id}/comments`
- `DELETE /api/posts/{id}`
- `GET /api/posts/admin` (ADMIN)
- `PATCH /api/posts/{id}/hidden` (ADMIN)
- `GET /api/users` (ADMIN)

## Pontos bons para adaptar na prova

Esse modelo é útil para qualquer tema com conteúdo criado por usuário: fórum, mural, chamados, avaliações, comentários, tarefas colaborativas e sistema de moderação.
