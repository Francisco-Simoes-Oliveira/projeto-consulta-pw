# DEPLOY + INTEGRAÇÃO COM IA — GUIA DE PROVA

Este arquivo é separado para consulta rápida quando a questão envolver publicação do sistema, variáveis de ambiente, banco em produção ou API de IA.

---

# 1. REGRA MAIS IMPORTANTE DO DEPLOY

Local:

```text
React: http://localhost:5173
Spring: http://localhost:8080
Banco: localhost
```

Produção:

```text
React: https://meu-front.vercel.app
Spring: https://meu-back.onrender.com
Banco: endereço do banco gerenciado
```

Portanto, quase todo deploy exige mudar **URLs e variáveis de ambiente**, não lógica de negócio.

---

# 2. PREPARAR O BACKEND

Use variáveis:

```properties
server.port=${PORT:8080}

spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

app.jwt.secret=${JWT_SECRET}
app.ai.key=${AI_API_KEY:}

spring.jpa.hibernate.ddl-auto=update
```

Não faça commit de:

```text
.env
application-secrets.properties
chaves API
senha banco
JWT secret
```

`.gitignore`:

```gitignore
.env
.env.*
application-secrets.properties
node_modules/
target/
dist/
```

---

# 3. DOCKERFILE PARA SPRING BOOT

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Render suporta construir serviços JVM via Dockerfile; isso evita depender de runtime Java nativo da plataforma.

Teste local:

```bash
docker build -t meu-backend .
docker run -p 8080:8080 --env-file .env meu-backend
```

---

# 4. DEPLOY BACKEND NO RENDER — ROTEIRO

1. Suba projeto no GitHub.
2. Crie Web Service.
3. Conecte repositório.
4. Se backend está numa subpasta, configure Root Directory para `backend`.
5. Use Docker se houver Dockerfile.
6. Cadastre variáveis de ambiente.
7. Configure banco de produção.
8. Faça deploy.
9. Abra `/swagger-ui/index.html` e teste.

Depois copie URL pública, por exemplo:

```text
https://meu-backend.onrender.com
```

---

# 5. CORS EM PRODUÇÃO

Não deixe apenas localhost.

```java
configuration.setAllowedOrigins(List.of(
    "http://localhost:5173",
    "https://meu-front.vercel.app"
));
```

Melhor por env:

```properties
app.frontend.url=${FRONTEND_URL:http://localhost:5173}
```

```java
@Value("${app.frontend.url}")
private String frontendUrl;
```

---

# 6. FRONTEND VITE NA VERCEL

`.env.production` ou variável no painel:

```env
VITE_API_URL=https://meu-backend.onrender.com
```

Código:

```javascript
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL
});
```

Build local:

```bash
npm run build
```

Se build falha local, provavelmente falhará no deploy.

Em SPA com React Router, configure rewrite para `index.html` quando necessário.

`vercel.json`:

```json
{
  "rewrites": [
    { "source": "/(.*)", "destination": "/index.html" }
  ]
}
```

---

# 7. BANCO EM PRODUÇÃO

Nunca use `localhost` do seu computador.

A plataforma/banco fornece host/URL. Exemplo conceitual:

```text
jdbc:postgresql://host:5432/database
```

ou MySQL:

```text
jdbc:mysql://host:3306/database
```

Variáveis:

```env
DATABASE_URL=jdbc:postgresql://...
DATABASE_USER=...
DATABASE_PASSWORD=...
```

Se provedor entrega URL estilo `postgres://user:pass@host/db`, talvez seja necessário converter para URL JDBC dependendo do driver/configuração.

---

# 8. CHECKLIST DE DEPLOY

- [ ] backend compila
- [ ] frontend `npm run build`
- [ ] secrets fora do Git
- [ ] banco de produção criado
- [ ] env vars cadastradas
- [ ] `server.port=${PORT:8080}`
- [ ] CORS aceita domínio do frontend
- [ ] frontend usa URL HTTPS pública
- [ ] Swagger funciona
- [ ] login funciona
- [ ] refresh em rota React não dá 404
- [ ] WebSocket usa URL de produção
- [ ] README tem links

---

# 9. IA — ARQUITETURA

Use:

```text
React
  ↓ POST /api/ai/insight
Spring Boot
  ↓ HTTPS + AI_API_KEY
Gemini / outro provedor
  ↓ resposta
Spring Boot
  ↓ JSON
React
```

Benefícios:
- chave não aparece no browser;
- você controla prompt;
- valida entrada;
- pode aplicar rate limit;
- consegue logar uso sem expor segredo.

---

# 10. EXEMPLO REAL: INSIGHT FINANCEIRO

Request:

```java
public record AiInsightRequest(
    @NotBlank String periodo
) {}
```

Response:

```java
public record AiInsightResponse(String texto) {}
```

Controller:

```java
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/insight")
    public AiInsightResponse insight(@Valid @RequestBody AiInsightRequest request,
                                     Authentication auth) {
        return new AiInsightResponse(
            aiService.gerarInsight(auth.getName(), request.periodo())
        );
    }
}
```

Service primeiro busca dados do usuário no próprio banco e envia só o necessário para IA.

---

# 11. CHAMANDO GEMINI POR REST NO SPRING

A API Gemini expõe `generateContent`. Mantenha modelo/endpoint configuráveis porque nomes de modelos podem mudar.

Properties:

```properties
app.ai.base-url=${AI_BASE_URL:https://generativelanguage.googleapis.com/v1beta}
app.ai.model=${AI_MODEL}
app.ai.key=${AI_API_KEY}
```

Configurar `RestClient`:

```java
@Configuration
public class HttpConfig {
    @Bean
    RestClient restClient() {
        return RestClient.create();
    }
}
```

Service genérico:

```java
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestClient restClient;

    @Value("${app.ai.base-url}")
    private String baseUrl;

    @Value("${app.ai.model}")
    private String model;

    @Value("${app.ai.key}")
    private String apiKey;

    public String perguntar(String prompt) {
        String url = baseUrl + "/models/" + model + ":generateContent?key=" + apiKey;

        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        Map resposta = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .body(Map.class);

        // Em projeto real, crie DTOs para mapear a resposta.
        return extrairTexto(resposta);
    }
}
```

Para prova, se o tempo for curto, `Map` funciona como demonstração. Em projeto final, prefira DTOs tipados.

---

# 12. PROMPT ÚTIL E CONTROLADO

Não mande “analise meus gastos” sem contexto. Monte uma tarefa objetiva:

```java
String prompt = """
Você é um assistente financeiro informativo.
Analise apenas os dados fornecidos.
Não invente transações.
Responda em português do Brasil com:
1. resumo em até 5 linhas;
2. três categorias de maior gasto;
3. duas observações objetivas.

Dados do período:
%s
""".formatted(dadosJson);
```

Melhor ainda: peça resposta JSON para processamento estruturado quando o provedor suportar.

---

# 13. O QUE NÃO ENVIAR PARA IA

Evite enviar desnecessariamente:
- senha;
- JWT;
- chave API;
- CPF/documentos;
- dados bancários completos;
- qualquer dado pessoal que a funcionalidade não precise.

Princípio: **mínimo necessário**.

---

# 14. IA NO REACT

```javascript
export async function gerarInsight(periodo) {
  const { data } = await api.post('/api/ai/insight', { periodo });
  return data;
}
```

```jsx
const [insight, setInsight] = useState('');
const [loading, setLoading] = useState(false);

async function analisar() {
  setLoading(true);
  try {
    const data = await gerarInsight('2026-09');
    setInsight(data.texto);
  } finally {
    setLoading(false);
  }
}
```

---

# 15. TRATANDO ERRO/LIMITE DE IA

Backend deve traduzir erro externo para resposta controlada.

```java
try {
   ... chamada ...
} catch (RestClientResponseException e) {
   throw new AiUnavailableException("Serviço de IA indisponível");
}
```

Não devolva stack trace/chave ao frontend.

Possíveis problemas:
- API key inválida;
- modelo incorreto;
- quota excedida;
- timeout;
- JSON de resposta mudou;
- rate limit.

---

# 16. RATE LIMIT PARA IA E LOGIN

A atividade exige atenção a rotas sensíveis. Mesmo que você não implemente biblioteca complexa na prova, saiba justificar:

```text
/login -> evita força bruta
/api/ai -> evita gasto/quota abusiva
/password-reset -> evita spam
```

Pode ser feito na aplicação, gateway ou plataforma.

---

# 17. WEBSOCKET EM PRODUÇÃO

Não deixe hardcoded:

```javascript
new SockJS('http://localhost:8080/ws')
```

Use:

```javascript
const wsBase = import.meta.env.VITE_API_URL;
new SockJS(`${wsBase}/ws`);
```

Cheque se a hospedagem suporta conexão WebSocket e se CORS/origin estão corretos.

---

# 18. README PARA ENTREGA

```md
# Nome do Projeto

## Tecnologias
- React + Vite
- Spring Boot
- Banco X
- JWT
- WebSocket/STOMP
- Gemini API

## Rodar local
### Backend
...
### Frontend
...

## Variáveis de ambiente
JWT_SECRET=
DATABASE_URL=
DATABASE_USER=
DATABASE_PASSWORD=
AI_API_KEY=
AI_MODEL=
FRONTEND_URL=

## Deploy
Frontend: ...
Backend: ...
Swagger: ...

## Funcionalidades em tempo real
...

## IA
Explique função real e como configurar chave.
```

---

# 19. FONTES OFICIAIS CONSULTADAS PARA A PARTE QUE MUDA COM O TEMPO

- Render Docs: deploy via Docker é suportado e indicado para runtimes JVM quando se quer ambiente reproduzível.
- Spring Framework Docs: WebSocket pode usar STOMP como subprotocolo para estruturar mensagens/destinos.
- Google AI for Developers: Gemini disponibiliza endpoint `models.generateContent` e streaming; modelo deve ser configurável para não depender de nome hardcoded.

Confirme nomes de modelo/planos no dia do deploy, pois isso muda com o tempo.
