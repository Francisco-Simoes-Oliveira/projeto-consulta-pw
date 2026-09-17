# CONSULTA DEFINITIVA — PROGRAMAÇÃO WEB
## React + Spring Boot + Validação + Integração + JWT + WebSocket + PWA + Swagger + Segurança

> Baseado na **Atividade Final – Parte Final** e no material de consulta anterior do professor.  
> Objetivo: servir como arquivo de consulta rápido durante a prova e como roteiro para adaptar um projeto base.

---

# 0. O QUE A ATIVIDADE DO TRIMESTRE INDICA QUE É MAIS PROVÁVEL CAIR

A atividade final exige, no mínimo:

1. Frontend React integrado ao backend Spring Boot, sem mocks.
2. Autenticação e persistência real em banco.
3. Comunicação em tempo real por WebSocket e/ou SSE.
4. Relatórios.
5. Integração com IA.
6. PWA: `manifest.json` + service worker/offline.
7. Segurança: validação, controle de acesso, prevenção de SQL Injection/XSS, CORS/CSRF conforme o caso, rate limiting em rotas sensíveis, headers e segredos fora do repositório.
8. Arquitetura organizada: Controller, Service, Repository, DTOs etc.
9. Deploy público.
10. README + Swagger/OpenAPI.

O material antigo fornecido pelo professor também contém exemplos de:
- comandos Maven/React;
- CORS;
- CRUD Controller/Service/Repository;
- `@Query`, `Pageable` e busca por email;
- Bean Validation;
- Spring Security;
- JWT;
- usuário autenticado via `SecurityContextHolder`;
- senha com BCrypt;
- relacionamento entre entidades;
- envio de e-mail.

Portanto, **CRUD + validação + integração + autenticação JWT + segurança + WebSocket** são os assuntos mais importantes para dominar.

---

# 1. COMANDOS DE SOBREVIVÊNCIA

## Spring Boot

```bash
# Windows
mvnw spring-boot:run
mvnw clean install
mvnw test

# Linux/macOS
./mvnw spring-boot:run
./mvnw clean install
```

Se der erro por dependência:

```bash
mvnw clean
mvnw dependency:purge-local-repository
mvnw clean install
```

## React com Vite

```bash
npm create vite@latest frontend -- --template react
cd frontend
npm install
npm run dev
npm run build
```

Dependências comuns:

```bash
npm install axios react-router-dom
npm install @stomp/stompjs sockjs-client
```

---

# 2. ARQUITETURA QUE VOCÊ DEVE MONTAR

## Backend

```text
src/main/java/com/exemplo/app/
├── config/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── exception/
├── repository/
├── security/
└── service/
```

Fluxo:

```text
React
  ↓ HTTP JSON
Controller
  ↓ chama
Service
  ↓ usa
Repository
  ↓ JPA
Banco
```

Regra prática:
- **Controller**: recebe HTTP, valida DTO, escolhe status HTTP.
- **Service**: regras de negócio.
- **Repository**: acesso ao banco.
- **Entity**: tabela/relacionamentos.
- **DTO**: dados que entram e saem da API.

Não coloque regra grande no Controller.

---

# 3. ENTITY JPA — MODELO COPIÁVEL

```java
@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatusItem status;
}
```

Enum:

```java
public enum StatusItem {
    ATIVO,
    INATIVO
}
```

## Relacionamentos

### Muitos para um

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "categoria_id", nullable = false)
private Categoria categoria;
```

### Um para muitos

```java
@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
private List<Item> items = new ArrayList<>();
```

### Muitos para muitos

```java
@ManyToMany
@JoinTable(
    name = "usuario_perfil",
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "perfil_id")
)
private Set<Perfil> perfis = new HashSet<>();
```

### Cuidado com loop JSON

Entidades bidirecionais podem virar:

```text
Categoria -> Item -> Categoria -> Item -> ...
```

Melhor solução: **DTOs**. Em emergência existem `@JsonIgnore`, `@JsonManagedReference` e `@JsonBackReference`, mas DTO normalmente é mais limpo.

---

# 4. DTO + VALIDAÇÃO

Nunca confie no React para validar sozinho. Validação importante deve existir no backend.

```java
public record ItemRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String nome,

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    BigDecimal valor,

    @NotNull(message = "Data é obrigatória")
    @PastOrPresent(message = "Data não pode estar no futuro")
    LocalDate data
) {}
```

Principais anotações:

| Anotação | Uso |
|---|---|
| `@NotNull` | não aceita `null` |
| `@NotBlank` | String não nula/não vazia/não só espaços |
| `@NotEmpty` | coleção/string não vazia |
| `@Size(min,max)` | tamanho |
| `@Email` | formato de e-mail |
| `@Min`, `@Max` | números inteiros |
| `@DecimalMin` | decimal mínimo |
| `@Positive` | > 0 |
| `@PositiveOrZero` | >= 0 |
| `@Past` | data passada |
| `@PastOrPresent` | passada ou hoje |
| `@Future` | futura |
| `@Pattern` | regex |

No Controller precisa de `@Valid`:

```java
@PostMapping
public ResponseEntity<ItemResponse> criar(@Valid @RequestBody ItemRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
}
```

Sem `@Valid`, as anotações do DTO podem não ser executadas na entrada.

---

# 5. REPOSITORY

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNomeContainingIgnoreCase(String nome);
    Optional<Item> findByNome(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
```

Spring cria consultas pelo nome do método.

## `@Query`

```java
@Query("select i from Item i where lower(i.nome) like lower(concat('%', :nome, '%'))")
List<Item> buscarPorNome(@Param("nome") String nome);
```

JPQL usa **nome da classe e atributos Java**, não necessariamente tabela/coluna SQL.

Paginação:

```java
Page<Item> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
```

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());
```

---

# 6. SERVICE — CRUD CORRETO

```java
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public List<Item> listar() {
        return repository.findAll();
    }

    public Item buscar(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }

    public Item criar(ItemRequest request) {
        Item item = new Item();
        item.setNome(request.nome());
        item.setValor(request.valor());
        item.setData(request.data());
        return repository.save(item);
    }

    public Item atualizar(Long id, ItemRequest request) {
        Item item = buscar(id);
        item.setNome(request.nome());
        item.setValor(request.valor());
        item.setData(request.data());
        return repository.save(item);
    }

    public void excluir(Long id) {
        Item item = buscar(id);
        repository.delete(item);
    }
}
```

**Erro comum:** atualizar fazendo apenas `save(requestConvertido)` e perder dados/relacionamentos. Melhor buscar a entidade existente e alterar campos permitidos.

---

# 7. CONTROLLER — CRUD REST

```java
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping
    public List<Item> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Item buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Item> criar(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/{id}")
    public Item atualizar(@PathVariable Long id,
                          @Valid @RequestBody ItemRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
```

## `@RequestParam` x `@PathVariable`

```java
GET /api/items/10
@GetMapping("/{id}")
buscar(@PathVariable Long id)
```

```java
GET /api/items/buscar?nome=mouse
@GetMapping("/buscar")
buscar(@RequestParam String nome)
```

---

# 8. STATUS HTTP QUE MAIS CAEM

| Código | Significado | Quando usar |
|---|---|---|
| 200 | OK | GET/PUT normal |
| 201 | Created | POST criou recurso |
| 204 | No Content | DELETE com sucesso |
| 400 | Bad Request | JSON/dados inválidos |
| 401 | Unauthorized | não autenticado/token inválido |
| 403 | Forbidden | autenticado, mas sem permissão |
| 404 | Not Found | recurso não existe |
| 409 | Conflict | duplicidade/conflito de regra |
| 422 | Unprocessable Entity | validação semântica, se projeto adotar |
| 500 | Internal Server Error | erro não tratado |

Decore: **401 = quem é você? 403 = sei quem é, mas você não pode.**

---

# 9. TRATAMENTO GLOBAL DE ERROS

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
            "status", 404,
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            erros.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(erros);
    }
}
```

React recebe algo previsível, por exemplo:

```json
{
  "nome": "Nome é obrigatório",
  "valor": "Valor deve ser maior que zero"
}
```

---

# 10. CORS

Se React roda em `5173` e Spring em `8080`, são origens diferentes.

Configuração global:

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

Solução rápida mencionada no material antigo:

```java
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ItemController { }
```

Prefira global em projeto maior.

---

# 11. REACT — ESTADO, FORMULÁRIO E EFEITOS

```jsx
const [items, setItems] = useState([]);
const [form, setForm] = useState({ nome: '', valor: '', data: '' });
const [loading, setLoading] = useState(false);
const [erro, setErro] = useState('');
```

Carregar ao abrir:

```jsx
useEffect(() => {
  carregar();
}, []);
```

Input controlado:

```jsx
<input
  name="nome"
  value={form.nome}
  onChange={(e) => setForm({ ...form, nome: e.target.value })}
/>
```

Handler genérico:

```jsx
function handleChange(e) {
  const { name, value } = e.target;
  setForm(prev => ({ ...prev, [name]: value }));
}
```

---

# 12. AXIOS — CONFIGURAÇÃO DEFINITIVA

`.env`:

```env
VITE_API_URL=http://localhost:8080
```

`src/api.js`:

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
    }
    return Promise.reject(error);
  }
);

export default api;
```

Service:

```javascript
import api from './api';

export const itemService = {
  listar: () => api.get('/api/items'),
  buscar: id => api.get(`/api/items/${id}`),
  criar: dados => api.post('/api/items', dados),
  atualizar: (id, dados) => api.put(`/api/items/${id}`, dados),
  excluir: id => api.delete(`/api/items/${id}`)
};
```

Uso:

```javascript
const resposta = await itemService.listar();
setItems(resposta.data);
```

---

# 13. CRUD REACT COMPLETO — PADRÃO

```jsx
function ItemsPage() {
  const [items, setItems] = useState([]);
  const [editandoId, setEditandoId] = useState(null);
  const [form, setForm] = useState({ nome: '', valor: '', data: '' });

  async function carregar() {
    const { data } = await itemService.listar();
    setItems(data);
  }

  useEffect(() => { carregar(); }, []);

  function editar(item) {
    setEditandoId(item.id);
    setForm({ nome: item.nome, valor: item.valor, data: item.data });
  }

  async function salvar(e) {
    e.preventDefault();
    if (editandoId) {
      await itemService.atualizar(editandoId, form);
    } else {
      await itemService.criar(form);
    }
    setEditandoId(null);
    setForm({ nome: '', valor: '', data: '' });
    await carregar();
  }

  async function excluir(id) {
    if (!confirm('Excluir?')) return;
    await itemService.excluir(id);
    await carregar();
  }

  return (...);
}
```

---

# 14. REACT ROUTER

```jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/items" element={<ProtectedRoute><ItemsPage /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  );
}
```

ProtectedRoute:

```jsx
import { Navigate } from 'react-router-dom';

export default function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" replace />;
}
```

**Isso melhora UX, mas a segurança real é no backend.** O usuário pode alterar JS no navegador.

---

# 15. AUTENTICAÇÃO JWT — COMO FUNCIONA

Fluxo:

```text
1. React envia email/senha -> POST /api/auth/login
2. Spring AuthenticationManager valida
3. Backend cria JWT
4. React guarda token
5. Axios envia Authorization: Bearer TOKEN
6. Filtro JWT roda antes do Controller
7. Token válido -> SecurityContext recebe usuário
8. Endpoint protegido é liberado
```

JWT normalmente contém:

```text
header.payload.signature
```

Não coloque senha ou dados secretos no payload. JWT assinado normalmente é **legível**, não criptografado.

---

# 16. USUÁRIO + BCRYPT

```java
@Entity
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Override
    public String getUsername() { return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
```

Na criação:

```java
usuario.setPassword(passwordEncoder.encode(request.password()));
```

Bean:

```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Nunca salve senha pura.

---

# 17. LOGIN COM SPRING SECURITY

```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    String token = jwtService.generateToken(auth.getName());
    return ResponseEntity.ok(new LoginResponse(token));
}
```

DTO:

```java
public record LoginRequest(
    @Email @NotBlank String email,
    @NotBlank String password
) {}

public record LoginResponse(String token) {}
```

---

# 18. SECURITY CONFIG

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

Por que `STATELESS`? Porque cada request leva o JWT; não dependemos de sessão HTTP do servidor.

---

# 19. JWT SERVICE — MODELO

Com JJWT moderno:

```java
@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long expiration;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expiration))
            .signWith(key())
            .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

Segredo no ambiente, nunca Git:

```properties
app.jwt.secret=${JWT_SECRET}
```

---

# 20. FILTRO JWT

```java
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
                                    throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (jwtService.isValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtService.getUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
```

---

# 21. PEGAR USUÁRIO AUTENTICADO

Material do professor usa `SecurityContextHolder`.

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String email = auth.getName();
Usuario usuario = usuarioRepository.findByEmail(email)
    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
```

Ou no Controller:

```java
@GetMapping("/me")
public Object me(Authentication authentication) {
    return authentication.getName();
}
```

**Para dados privados, jamais confie num `userId` enviado pelo React se você pode descobrir o usuário pelo JWT.**

---

# 22. AUTORIZAÇÃO / BROKEN ACCESS CONTROL

Erro grave:

```java
public Item buscar(Long id) {
    return repository.findById(id).orElseThrow();
}
```

Se Item pertence a usuário, qualquer usuário poderia tentar IDs alheios.

Melhor:

```java
Optional<Item> findByIdAndUsuario(Long id, Usuario usuario);
```

```java
Item item = repository.findByIdAndUsuario(id, usuarioAtual())
    .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
```

Também pode usar:

```java
@PreAuthorize("hasRole('ADMIN')")
```

ou:

```java
@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
```

---

# 23. LOGIN NO REACT

```jsx
async function handleLogin(e) {
  e.preventDefault();
  try {
    const { data } = await api.post('/api/auth/login', { email, password });
    localStorage.setItem('token', data.token);
    navigate('/');
  } catch (err) {
    setErro(err.response?.data?.message || 'Falha no login');
  }
}
```

Se API envelopa resposta:

```json
{ "success": true, "data": { "token": "..." } }
```

então:

```js
localStorage.setItem('token', response.data.data.token);
```

Sempre confira o formato real da resposta.

---

# 24. WEBSOCKET + STOMP — CONCEITO

HTTP normal:

```text
cliente pede -> servidor responde -> conexão da operação termina
```

WebSocket:

```text
cliente <=================> servidor
       conexão persistente
```

STOMP organiza destinos/tópicos em cima do WebSocket.

Exemplo útil da atividade: quando alguém cria uma transação numa carteira compartilhada, o backend salva e publica evento; outros membros recebem e atualizam dashboard sem F5.

---

# 25. WEBSOCKET NO SPRING

Dependência:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

Config:

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }
}
```

Enviar evento de um Service:

```java
private final SimpMessagingTemplate messagingTemplate;

public Item criar(ItemRequest request) {
    Item salvo = repository.save(...);
    messagingTemplate.convertAndSend("/topic/items", salvo);
    return salvo;
}
```

Ou por carteira:

```java
messagingTemplate.convertAndSend(
    "/topic/wallet/" + walletId,
    evento
);
```

Isso é melhor do que um tópico global quando há dados privados.

---

# 26. WEBSOCKET NO REACT

```bash
npm install @stomp/stompjs sockjs-client
```

```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const client = new Client({
  webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
  reconnectDelay: 5000,
  onConnect: () => {
    client.subscribe('/topic/items', message => {
      const evento = JSON.parse(message.body);
      console.log(evento);
    });
  }
});

client.activate();
```

Em componente React:

```jsx
useEffect(() => {
  const client = criarCliente();
  client.onConnect = () => {
    client.subscribe('/topic/items', () => carregar());
  };
  client.activate();

  return () => client.deactivate();
}, []);
```

O cleanup evita conexões duplicadas quando componente desmonta/remonta.

---

# 27. SSE — ALTERNATIVA

SSE serve quando o servidor só precisa empurrar eventos **servidor -> cliente**.

Backend:

```java
@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> events() { ... }
```

Frontend:

```javascript
const source = new EventSource('/api/events');
source.onmessage = e => console.log(e.data);
```

WebSocket é bidirecional; SSE é mais simples para notificações/atualizações unidirecionais.

---

# 28. RELATÓRIO

Um relatório pode ser endpoint agregado:

```java
public record ResumoMensal(
    BigDecimal receitas,
    BigDecimal despesas,
    BigDecimal saldo
) {}
```

Repository:

```java
@Query("""
    select coalesce(sum(t.valor), 0)
    from Transacao t
    where t.usuario = :usuario
      and t.tipo = :tipo
      and t.data between :inicio and :fim
""")
BigDecimal totalPeriodo(...);
```

No React, relatório pode ser tabela, cards, gráfico ou exportação.

Critério da atividade não exige necessariamente PDF; exige **geração de pelo menos um relatório**.

---

# 29. PWA — MÍNIMO PARA PROVA

Vite pode usar plugin ou fazer manualmente.

`public/manifest.json`:

```json
{
  "name": "Minha Aplicação",
  "short_name": "MinhaApp",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#111827",
  "icons": [
    { "src": "/icon-192.png", "sizes": "192x192", "type": "image/png" },
    { "src": "/icon-512.png", "sizes": "512x512", "type": "image/png" }
  ]
}
```

No `index.html`:

```html
<link rel="manifest" href="/manifest.json" />
<meta name="theme-color" content="#111827" />
```

Service worker mínimo `public/sw.js`:

```javascript
const CACHE = 'app-v1';
const ASSETS = ['/', '/index.html'];

self.addEventListener('install', event => {
  event.waitUntil(caches.open(CACHE).then(cache => cache.addAll(ASSETS)));
});

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request).then(cached => cached || fetch(event.request))
  );
});
```

Registrar em `main.jsx`:

```javascript
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => navigator.serviceWorker.register('/sw.js'));
}
```

---

# 30. SWAGGER / OPENAPI

Dependência comum:

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.8.9</version>
</dependency>
```

URLs típicas:

```text
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

Se Spring Security estiver ativo, libere essas rotas.

---

# 31. SEGURANÇA — RESUMO OWASP PARA A ATIVIDADE

## SQL Injection

Com JPA Repository e parâmetros, evite concatenar SQL manualmente.

Ruim:

```java
"select * from user where email='" + email + "'"
```

Bom:

```java
findByEmail(email)
```

ou parâmetros em query.

## XSS

- React escapa texto interpolado por padrão.
- Evite `dangerouslySetInnerHTML` com entrada do usuário.
- Não salve HTML arbitrário sem sanitizar.

## Broken Access Control

Sempre verifique proprietário/perfil no backend.

## Senhas

BCrypt; nunca texto puro.

## Segredos

Ruim:

```properties
api.key=MINHA_CHAVE_REAL
```

Bom:

```properties
api.key=${AI_API_KEY}
```

`.env` e arquivos de segredo no `.gitignore`.

## CSRF

Em API REST stateless com JWT no header Authorization, é comum desabilitar CSRF. Se autenticação estiver baseada em cookies/sessão, a análise muda.

## Rate limiting

Especialmente `/login`, recuperação de senha e IA. Pode ser implementado via Bucket4j, gateway/reverse proxy ou infraestrutura.

---

# 32. CONFIGURAÇÃO POR AMBIENTE

Backend:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/app}
spring.datasource.username=${DATABASE_USER:root}
spring.datasource.password=${DATABASE_PASSWORD:}
app.jwt.secret=${JWT_SECRET}
app.ai.key=${AI_API_KEY:}
```

Frontend:

```env
VITE_API_URL=http://localhost:8080
```

Em produção:

```env
VITE_API_URL=https://seu-backend.exemplo.com
```

**VITE_* vai para o navegador. Nunca coloque segredo de IA/JWT no frontend.**

---

# 33. INTEGRAÇÃO COM IA — ARQUITETURA SEGURA

Correto:

```text
React -> seu Spring Boot -> API de IA
```

Errado para chave privada:

```text
React -> API de IA com chave hardcoded
```

Endpoint:

```java
@PostMapping("/api/ai/insight")
public InsightResponse insight(@RequestBody InsightRequest request) {
    return aiService.gerarInsight(request);
}
```

Service monta um prompt com dados necessários e chama provedor usando chave do backend.

Veja o arquivo `DEPLOY_E_IA.md` para exemplo completo.

---

# 34. TESTES

Service unitário:

```java
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock ItemRepository repository;
    @InjectMocks ItemService service;

    @Test
    void deveBuscarItem() {
        Item item = new Item();
        item.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertEquals(1L, service.buscar(1L).getId());
    }
}
```

Controller/integration pode usar `MockMvc`.

---

# 35. ERROS MAIS COMUNS NA INTEGRAÇÃO

## Network Error
- backend não iniciou;
- porta errada;
- CORS;
- URL errada no `.env`;
- HTTPS frontend chamando HTTP backend.

## 401
- não enviou Bearer;
- token expirou;
- interceptor não está sendo usado;
- chave JWT diferente;
- endpoint deveria ser `permitAll` mas não é.

## 403
- token reconhecido, sem autorização;
- role diferente de `ROLE_ADMIN` x `ADMIN`;
- CSRF/config de segurança.

## 400
- JSON não bate com DTO;
- `@Valid` rejeitou;
- data/formato inválido.

## 404
- rota errada;
- id inexistente;
- `/api` faltando.

## 415 Unsupported Media Type
- `Content-Type` errado;
- enviou form-data para endpoint JSON ou vice-versa.

## React atualiza tela, banco não
- você alterou apenas estado local/mock;
- faltou `await service.criar`;
- erro foi engolido no catch.

---

# 36. PATCH x PUT

- `PUT`: normalmente substituição/atualização completa do recurso.
- `PATCH`: atualização parcial.

Exemplo PATCH simples:

```java
@PatchMapping("/{id}/status")
public Item alterarStatus(@PathVariable Long id,
                          @RequestBody StatusRequest request) {
    return service.alterarStatus(id, request.status());
}
```

---

# 37. FORM-DATA / UPLOAD DE ARQUIVO

Backend:

```java
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> upload(@RequestPart("arquivo") MultipartFile arquivo) {
    return ResponseEntity.ok(...);
}
```

React:

```javascript
const formData = new FormData();
formData.append('arquivo', file);
await api.post('/api/upload', formData);
```

Não force manualmente boundary do multipart; o browser/axios cuida.

---

# 38. EMAIL — PADRÃO PRESENTE NO MATERIAL DO PROFESSOR

```java
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviar(String para, String assunto, String texto) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(para);
        msg.setSubject(assunto);
        msg.setText(texto);
        mailSender.send(msg);
    }
}
```

Pode ser usado em recuperação de senha/código de validação.

---

# 39. CHECKLIST: “A QUESTÃO PEDIU UM CRUD NOVO”

1. Criar Entity.
2. Criar Repository extends `JpaRepository`.
3. Criar DTO request com validações.
4. Criar Service.
5. Criar Controller REST.
6. Tratar 404/validação.
7. Criar service Axios no React.
8. Criar página com `useState/useEffect`.
9. Integrar criar/listar/editar/excluir.
10. Se protegido, garantir Bearer token.
11. Testar no Swagger antes de culpar o React.

---

# 40. CHECKLIST: “A QUESTÃO PEDIU LOGIN JWT”

Backend:
1. Entity usuário + email único + password BCrypt.
2. `UserDetails` / `UserDetailsService`.
3. `PasswordEncoder`.
4. `AuthenticationManager`.
5. `JwtService`.
6. `JwtAuthFilter`.
7. `SecurityFilterChain` stateless.
8. `/api/auth/login` permitAll.

Frontend:
1. POST login.
2. guardar token.
3. Axios interceptor Bearer.
4. ProtectedRoute.
5. logout remove token.

---

# 41. CHECKLIST: “A QUESTÃO PEDIU TEMPO REAL”

1. starter WebSocket.
2. `@EnableWebSocketMessageBroker`.
3. endpoint `/ws`.
4. broker `/topic`.
5. `SimpMessagingTemplate` no service.
6. publicar evento depois de salvar.
7. React conecta via STOMP.
8. React subscribe no tópico.
9. cleanup/deactivate no `useEffect`.
10. Dados privados: não use tópico global sem controle.

---

# 42. CHECKLIST: “A QUESTÃO PEDIU VALIDAÇÃO”

Backend:
- starter validation;
- anotações no DTO;
- `@Valid` no Controller;
- `@RestControllerAdvice` para resposta amigável.

Frontend:
- `required`, min/max etc. para UX;
- mostrar mensagem retornada pelo backend;
- nunca depender apenas do frontend.

---

# 43. COMO ADAPTAR O PROJETO-BASE EM 5–10 MINUTOS

Suponha que a questão peça **Livro** com `titulo`, `autor`, `preco`.

No projeto-base:

1. Copie/renomeie pacote `item` -> `livro`.
2. `Item` -> `Livro`.
3. Troque campos da entity.
4. Troque campos de `ItemRequest`.
5. Ajuste mapeamento no Service.
6. `/api/items` -> `/api/livros`.
7. No React, copie `ItemsPage` -> `LivrosPage`.
8. `itemService` -> `livroService` e endpoint.
9. Troque inputs/tabela.
10. Teste primeiro no Swagger, depois no React.

O padrão estrutural permanece quase igual para Cliente, Produto, Livro, Tarefa, Transação, Categoria etc.

---

# 44. MAPA MENTAL DE ANOTAÇÕES SPRING

```text
@SpringBootApplication -> inicia app
@Entity               -> classe persistida
@Table                 -> nome/config da tabela
@Id                    -> chave primária
@GeneratedValue        -> geração id
@Column                -> coluna
@ManyToOne             -> N:1
@OneToMany             -> 1:N
@Repository            -> camada banco (interfaces JPA geralmente não precisam)
@Service               -> regra de negócio
@RestController        -> controller REST
@RequestMapping        -> prefixo rota
@GetMapping            -> GET
@PostMapping           -> POST
@PutMapping            -> PUT
@PatchMapping          -> PATCH
@DeleteMapping         -> DELETE
@RequestBody           -> JSON -> objeto
@PathVariable          -> parte da URL
@RequestParam          -> ?chave=valor
@Valid                 -> dispara Bean Validation
@Value                 -> lê property/env
@Bean                  -> objeto gerenciado Spring
@Configuration         -> classe de config
@Autowired             -> injeção (prefira construtor)
@PreAuthorize          -> autorização por método
@RestControllerAdvice  -> tratamento global
@ExceptionHandler      -> captura exceção
```

---

# 45. “NÃO FUNCIONA”: ORDEM DE DIAGNÓSTICO NA PROVA

1. Backend iniciou sem stack trace?
2. Banco conectou?
3. Swagger abre?
4. Endpoint funciona no Swagger/Postman?
5. JSON enviado bate com DTO?
6. Se protegido, token está correto?
7. React aponta para URL correta?
8. DevTools > Network mostra qual status?
9. CORS aparece no console?
10. `response.data` está no nível correto?
11. Estado React está sendo atualizado?
12. WebSocket abriu conexão e subscribe correto?

**Sempre separe problema de backend de problema de frontend. Teste o backend sozinho primeiro.**

---

# 46. COLA ULTRARRÁPIDA

```java
// Repository
interface XRepository extends JpaRepository<X, Long> {}

// Buscar ou 404
repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não encontrado"));

// Controller POST
@PostMapping
ResponseEntity<X> criar(@Valid @RequestBody XRequest r) {
  return ResponseEntity.status(201).body(service.criar(r));
}

// DELETE
@DeleteMapping("/{id}")
ResponseEntity<Void> excluir(@PathVariable Long id) {
  service.excluir(id);
  return ResponseEntity.noContent().build();
}
```

```javascript
// GET
const { data } = await api.get('/api/x');

// POST
await api.post('/api/x', form);

// PUT
await api.put(`/api/x/${id}`, form);

// DELETE
await api.delete(`/api/x/${id}`);
```

```http
Authorization: Bearer SEU_TOKEN
Content-Type: application/json
```

---

# 47. O QUE ESTÁ NO MATERIAL DO PROFESSOR x COMPLEMENTO

## Diretamente sustentado pelos arquivos fornecidos
- CRUD Spring com Controller/Service/Repository.
- Bean Validation.
- `@Query`/Pageable.
- Spring Security/JWT.
- BCrypt.
- usuário autenticado pelo `SecurityContextHolder`.
- CORS.
- integração React/Spring.
- WebSocket/SSE como requisito da atividade final.
- IA, PWA, Swagger, deploy, OWASP, relatório como requisitos da atividade final.

## Complementos deste guia
- modelos genéricos reorganizados para consulta rápida;
- padrões de React/Axios;
- projeto-base adaptável;
- checklist de diagnóstico;
- exemplos genéricos de PWA, WebSocket e IA;
- orientação de deploy atual.

---

# 48. RECOMENDAÇÃO PARA O REPOSITÓRIO DE CONSULTA

Comite esta estrutura:

```text
consulta-prova/
├── CONSULTA_DEFINITIVA.md
├── DEPLOY_E_IA.md
├── PROJETO_BASE/
│   ├── backend/
│   └── frontend/
└── README.md
```

Na prova, pesquise no editor por:
- `JWT`
- `WEBSOCKET`
- `VALIDAÇÃO`
- `CORS`
- `CRUD REACT`
- `STATUS HTTP`
- `DEPLOY`
- `IA`

Assim você encontra a seção sem perder tempo.
