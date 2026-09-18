# 09 — Deploy (colocar no ar)

**Requisito 1.8.** Link público funcionando, no README.

Combinação mais tranquila e gratuita:

| Parte | Serviço | Por quê |
|---|---|---|
| Frontend React | **Vercel** | Conecta no GitHub, dá build sozinho e entrega HTTPS (necessário para o PWA) |
| Backend Spring | **Render** | Aceita Dockerfile, tem plano gratuito |
| Banco | **Railway**, **Aiven** ou **Clever Cloud** | MySQL gerenciado no plano gratuito |

## 1. Banco

Crie um MySQL no serviço escolhido e anote: host, porta, nome do banco, usuário e senha.
No deploy, use `spring.jpa.hibernate.ddl-auto=update` só na primeira subida; depois troque para
`validate` para não mexer no schema sem querer.

## 2. Backend no Render

1. Suba o projeto no GitHub com o [Dockerfile](Dockerfile) na raiz do backend.
2. No Render: **New → Web Service → Build from a Dockerfile**.
3. Em **Environment**, cadastre as variáveis (as mesmas do `application-exemplo.properties`):

```
DB_URL       = jdbc:mysql://host:porta/financas?useSSL=true&serverTimezone=America/Sao_Paulo
DB_USER      = seu_usuario
DB_PASSWORD  = sua_senha
JWT_SECRET   = chave-grande-de-no-minimo-32-caracteres
GEMINI_API_KEY = sua-chave
```

4. Anote a URL gerada (ex.: `https://financas-api.onrender.com`) e teste
   `https://.../swagger-ui.html`.

## 3. Frontend na Vercel

1. **New Project → importe o repositório** e aponte o diretório do front.
2. Variável de ambiente: `REACT_APP_API_URL = https://financas-api.onrender.com/api/`.
3. No código, o axios lê essa variável:

```js
const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL || "http://localhost:8080/api/",
});
```

4. Depois do deploy, volte no backend e coloque o domínio da Vercel no CORS
   (`ConfiguracaoSeguranca.configuracaoCors()`) e no `setAllowedOriginPatterns` do WebSocket.

## 4. Rodando tudo local com Docker (opcional, mas rende ponto)

```bash
docker compose up --build
```

O [docker-compose.yml](docker-compose.yml) sobe MySQL + backend juntos. Serve para provar que o
projeto roda em qualquer máquina sem instalar MySQL.

## Armadilhas do plano gratuito

- **O Render "dorme"** depois de ~15 min sem acesso: a primeira chamada demora ~30s. Abra o sistema
  alguns minutos antes de apresentar.
- **Cold start + SSE/WebSocket**: teste a conexão em tempo real *no ambiente publicado*, não só local.
- **HTTPS obrigatório para PWA**: a Vercel já resolve; se o backend for HTTP, o navegador bloqueia a
  chamada (*mixed content*) — use a URL `https` do Render.
- **Nunca** suba `.env`, senha ou chave no GitHub. Se subir, troque o segredo.

## Se o banco gratuito for Postgres (Supabase, Neon)

Só muda o driver e a URL:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

```properties
spring.datasource.url=jdbc:postgresql://host:5432/financas
```

O JPA cuida do resto — é uma das vantagens de ter usado repository em vez de SQL na mão.
