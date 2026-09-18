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

## Correção SockJS + Vite: `global is not defined`

O `sockjs-client` ainda referencia a variável global do ambiente Node em alguns módulos.
No Vite, o projeto já possui esta compatibilidade em `frontend/vite.config.js`:

```js
define: {
  global: 'globalThis'
}
```

Se você estava com o servidor Vite aberto antes da alteração, pare o processo e execute novamente.
Se o cache continuar usando o bundle antigo, rode:

```bash
cd frontend
npm run dev -- --force
```

Ou apague `node_modules/.vite` e inicie novamente.

## Atualização: Dashboard, filtros e relatórios

O frontend agora possui navegação lateral com três áreas:

- **Dashboard**: indicadores, status dos itens, gráfico simples, maiores preços e últimos registros.
- **Itens / CRUD**: cadastro + edição + exclusão + filtros por texto/status/faixa de preço + ordenação.
- **Relatórios**: filtros, resumo, tabela pronta para impressão, exportação CSV e opção de salvar como PDF pelo navegador.

Nenhuma biblioteca de gráfico ou PDF foi adicionada. Isso foi proposital para reduzir dependências durante a prova.

O material `docs/CONSULTA_DEFINITIVA.md` também possui uma nova seção extensa de React/front-end.

---

## Correção V4 — WebSocket CORS + MySQL/phpMyAdmin

### WebSocket / SockJS
Se o navegador mostrar:

```text
Access-Control-Allow-Credentials ... must be 'true'
```

o `SecurityConfig` precisa ter:

```java
c.setAllowedOrigins(List.of("http://localhost:5173"));
c.setAllowCredentials(true);
```

Não use `*` em `allowedOrigins` quando `allowCredentials(true)` estiver ativo.

### Banco e phpMyAdmin
A V4 usa **MySQL/MariaDB por padrão**, portanto os registros aparecem no phpMyAdmin no banco:

```text
prova_base
```

Configuração padrão:

```text
host: localhost
porta: 3306
banco: prova_base
usuario: root
senha: vazia
```

Você pode trocar sem editar código:

```text
DB_URL=jdbc:mysql://localhost:3306/outro_banco
DB_USER=usuario
DB_PASSWORD=senha
```

Se estiver usando XAMPP/WAMP, inicie o MySQL antes do backend.

### Usar H2 como alternativa
O H2 continua disponível como perfil de emergência:

```powershell
cd backend
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Nesse modo os dados ficam no arquivo `backend/data/provabase.mv.db` e **não aparecem no phpMyAdmin**.
