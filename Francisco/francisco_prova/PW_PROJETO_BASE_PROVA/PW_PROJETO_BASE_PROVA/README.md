# PW — Projeto Base para Prova

Projeto **literal e executável** para servir como molde durante a prova.

## O que já vem funcionando

- React + Vite
- Axios e integração REST
- Spring Boot
- JPA + H2 persistente em arquivo
- CRUD completo de `Item`
- pesquisa por nome
- Bean Validation (`@NotBlank`, `@Size`, `@DecimalMin`...)
- tratamento global de erros
- cadastro/login
- BCrypt
- JWT Bearer
- interceptor Axios que coloca o token automaticamente
- Spring Security
- WebSocket + STOMP + SockJS
- Swagger/OpenAPI
- CORS para `localhost:5173`

## Login pronto

- e-mail: `admin@teste.com`
- senha: `123456`

## Rodar backend

Requisito: Java 21.

Windows:

```bash
cd backend
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
cd backend
chmod +x mvnw
./mvnw spring-boot:run
```

Backend: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui.html
H2 console: http://localhost:8080/h2-console

## Rodar frontend

Requisitos: Node + npm.

```bash
cd frontend
npm install
npm run dev
```

Frontend: http://localhost:5173

## COMO ADAPTAR RAPIDAMENTE NA PROVA

Se a questão pedir `Produto`, `Livro`, `Aluno`, `Tarefa`, etc., procure por `Item` e use como modelo.

Backend, na ordem:

1. `item/Item.java` → campos e `@Entity`.
2. `item/dto/ItemRequest.java` → campos recebidos + validações.
3. `item/ItemRepository.java` → consultas extras.
4. `item/ItemService.java` → regras de negócio.
5. `item/ItemController.java` → endpoints.

Frontend:

1. `services/itemService.js` → endpoints.
2. `pages/ItemsPage.jsx` → formulário e tabela.

Você pode simplesmente duplicar a pasta `item` e criar outra feature.

## Fluxo JWT

`POST /api/auth/login` → backend autentica senha → cria JWT → React salva token → interceptor Axios envia:

```http
Authorization: Bearer TOKEN
```

`JwtAuthFilter` lê o Bearer token antes dos controllers.

## Fluxo WebSocket

Spring publica em:

```text
/topic/items
```

Frontend se conecta em:

```text
/ws
```

Toda inclusão/alteração/exclusão chama `SimpMessagingTemplate` e outras abas atualizam automaticamente.

## Banco

Está usando H2 de propósito para a prova não depender de MySQL/PostgreSQL instalado.

Arquivo criado em:

```text
backend/data/
```

Para trocar por MySQL/PostgreSQL, altere dependência do `pom.xml` e `application.properties`.

## Segurança importante

O segredo JWT em `application.properties` é apenas para desenvolvimento. Em deploy use:

```bash
JWT_SECRET=uma-chave-longa-e-secreta
```

## Pastas

```text
PW_PROJETO_BASE_PROVA/
├── backend/
│   └── src/main/java/br/com/provabase/
│       ├── auth/       # JWT, login, usuário
│       ├── common/     # tratamento de erros
│       ├── config/     # Security e WebSocket
│       └── item/       # CRUD que você adapta
├── frontend/
│   └── src/
│       ├── pages/
│       └── services/
└── docs/
```
