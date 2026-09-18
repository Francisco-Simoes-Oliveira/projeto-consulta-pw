# Guia Completo dos Simulados: React & Spring Boot

Este documento contém todas as instruções técnicas para executar, configurar e validar os dois simulados desenvolvidos conforme os escopos avaliativos de Programação Web.

---

## 1. Estrutura de Pastas dos Simulados

Ambos os simulados foram criados de forma independente e isolada, cada um com seu próprio backend (Spring Boot) e frontend (React com Vite):

```
Controle-Financeiro/
├── PROJETO_COMPLETO_REACT_SPRING.pdf   (Documento técnico oficial gerado)
├── README_SIMULADOS.md                 (Este guia detalhado)
│
├── simulado1/                          (SIMULADO 1: Cálculo de Investimento)
│   ├── backend/
│   │   ├── pom.xml                     (Dependências Maven: Spring Boot, JPA, Security, JWT, Lombok)
│   │   └── src/main/
│   │       ├── resources/
│   │       │   └── application.properties  (Porta 8081, configurações H2 e MySQL)
│   │       └── java/com/simulado1/
│   │           ├── Simulado1Application.java
│   │           ├── config/SecurityConfig.java (Spring Security, CORS liberado)
│   │           ├── security/           (JwtUtils e JwtAuthenticationFilter)
│   │           ├── controller/         (InvestimentoController e AuthController)
│   │           ├── service/            (InvestimentoService - lógica dos juros compostos)
│   │           ├── repository/         (InvestimentoRepository - consultas JPA)
│   │           ├── model/              (Investimento - entidade do banco)
│   │           ├── dto/                (InvestimentoDTO - validações Bean Validation)
│   │           └── exception/          (GlobalExceptionHandler)
│   └── frontend/
│       ├── package.json                (React 19, Vite, Axios, Lucide React, React Router 7)
│       ├── vite.config.js              (Porta 5173)
│       └── src/
│           ├── config/axiosConfig.js   (Instância do Axios apontando para porta 8081)
│           ├── services/               (BaseService e investimentoService)
│           ├── components/Header.jsx   (Header reutilizável com título parametrizável)
│           ├── pages/
│           │   ├── InvestimentoHome.jsx    (Saudação com data/hora e redirecionamento)
│           │   └── InvestimentoCalculo.jsx (Formulário, cálculo no back, tabela e filtros)
│           ├── App.jsx & main.jsx
│           └── index.css               (Visual inspirado no mockup com abas Windows)
│
└── simulado2/                          (SIMULADO 2: Monitoramento de Atividade Física)
    ├── backend/
    │   ├── pom.xml                     (Dependências Maven idênticas)
    │   └── src/main/
    │       ├── resources/
    │       │   └── application.properties  (Porta 8082, configurações H2 e MySQL)
    │       └── java/com/simulado2/
    │           ├── Simulado2Application.java
    │           ├── config/SecurityConfig.java
    │           ├── security/           (JwtUtils e JwtAuthenticationFilter)
    │           ├── controller/         (AtividadeController e AuthController)
    │           ├── service/            (AtividadeService - velocidade e faixas)
    │           ├── repository/         (AtividadeRepository)
    │           ├── model/              (Atividade - entidade do banco)
    │           ├── dto/                (AtividadeDTO - validações > 0)
    │           └── exception/          (GlobalExceptionHandler)
    └── frontend/
        ├── package.json                (React 19, Vite, Axios, Lucide React, React Router 7)
        ├── vite.config.js              (Porta 5174 para evitar conflito)
        └── src/
            ├── config/axiosConfig.js   (Instância do Axios apontando para porta 8082)
            ├── services/               (BaseService e atividadeService)
            ├── components/Header.jsx   (Header reutilizável parametrizável)
            ├── pages/
            │   ├── AtividadeHome.jsx    (Saudação com data/hora e botão Registrar)
            │   └── AtividadeCalculo.jsx (Formulário km/min, cálculo, tabela e limpeza)
            ├── App.jsx & main.jsx
            └── index.css               (Design System moderno responsivo)
```

---

## 2. Configuração de Portas (Sem Conflitos)

Para permitir testar ambos os simulados simultaneamente sem conflito de portas, a distribuição padrão é:

| Módulo | Backend (Spring Boot) | Frontend (React / Vite) | Banco H2 Console |
| :--- | :--- | :--- | :--- |
| **Simulado 1** (Investimento) | `http://localhost:8081` | `http://localhost:5173` | `http://localhost:8081/h2-console` |
| **Simulado 2** (Atividade Física) | `http://localhost:8082` | `http://localhost:5174` | `http://localhost:8082/h2-console` |

> **Nota:** Se você alterar a porta do backend no `application.properties`, lembre-se de atualizar também o arquivo `src/config/axiosConfig.js` do frontend correspondente.

---

## 3. Como Preencher o `application.properties`

O arquivo `application.properties` fica localizado em:
- Simulado 1: `simulado1/backend/src/main/resources/application.properties`
- Simulado 2: `simulado2/backend/src/main/resources/application.properties`

### Opção A: Banco H2 em Memória (Padrão Ativo - Recomendado para Teste Rápido)
Não requer instalação de nenhum software extra. O banco é criado e reiniciado na memória a cada execução:

```properties
server.port=8081

# Conexão H2 em memória
spring.datasource.url=jdbc:h2:mem:investimentodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Habilitar console web do H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Chave JWT de 256 bits
jwt.secret=minhaChaveSuperSecretaParaAssinaturaJwtDeInvestimentosEAtividades2026!
jwt.expiration=86400000
```

---

### Opção B: Conectar ao Banco MySQL Local

Caso deseje persistir os dados no MySQL local (ex.: MySQL Workbench, XAMPP ou Docker):

#### Passo 1: Criar a base de dados no MySQL
Abra seu terminal MySQL ou MySQL Workbench e execute o comando SQL:
```sql
-- Para o Simulado 1:
CREATE DATABASE investimentodb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Para o Simulado 2:
CREATE DATABASE atividadedb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Passo 2: Ajustar o `application.properties`
Comente o bloco do H2 colocando `#` no início das linhas e descomente/preencha o bloco do MySQL:

```properties
server.port=8081

# Configuração MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/investimentodb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=seu_usuario_aqui    # Geralmente: root
spring.datasource.password=sua_senha_aqui      # Senha que você definiu na instalação do MySQL

# Hibernate / JPA
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Desabilitar console H2 se usar MySQL
spring.h2.console.enabled=false

# Chave JWT de 256 bits
jwt.secret=minhaChaveSuperSecretaParaAssinaturaJwtDeInvestimentosEAtividades2026!
jwt.expiration=86400000
```

---

## 4. Como Executar os Projetos

### Executando o Simulado 1 (Investimento)

#### Terminal 1 - Backend:
```bash
cd simulado1/backend
mvn clean spring-boot:run
```
*O backend subirá na porta **8081**.*

#### Terminal 2 - Frontend:
```bash
cd simulado1/frontend
npm install
npm run dev
```
*O frontend abrirá na porta **5173** (`http://localhost:5173`).*

---

### Executando o Simulado 2 (Atividade Física)

#### Terminal 1 - Backend:
```bash
cd simulado2/backend
mvn clean spring-boot:run
```
*O backend subirá na porta **8082**.*

#### Terminal 2 - Frontend:
```bash
cd simulado2/frontend
npm install
npm run dev
```
*O frontend abrirá na porta **5174** (`http://localhost:5174`).*

---

## 5. Roteiro de Validação para Apresentação ao Professor

### Simulado 1: Cálculo de Investimento (PDF 1)
1. **Home (`/`):**
   - Verifique se exibe: `"Olá, você acessou esta página dia DD/MM/AAAA às HH:MM"`.
   - Clique no botão `"Realizar o Calculo de Investimento"` para navegar para `/calculo`.
2. **Reutilização do Header:**
   - Observe o título no topo exibindo: `"Cálculo de Investimento - Listagem"` (parâmetro reutilizável).
3. **Cálculo no Backend (Não no Frontend):**
   - Informe: Valor Inicial = `100`, Prazo = `12`, Juro Mensal = `5`.
   - Clique em `"Calcular"`.
   - Verifique o texto logo abaixo do botão: `"o valor final será de 179,59"`.
4. **Salvar no Banco:**
   - Clique em `"Salvar Cálculo"`. O cálculo será persistido no banco e inserido na tabela abaixo.
5. **Tabela e Filtros:**
   - Ao abrir a página, todos os registros existentes são carregados automaticamente.
   - Teste o filtro por data selecionando a data do cálculo e clicando em `"Pesquisar"`.
   - Teste o filtro por prazo/juro digitando `12` ou `5` e clicando em `"Pesquisar"`.
6. **Limpeza da Tabela:**
   - Clique no botão vermelho `"Limpar Dados"`. Confirme a exclusão e veja que todos os registros são deletados do banco de dados via endpoint `DELETE`.

---

### Simulado 2: Monitoramento de Atividade Física (PDF 2)
1. **Home (`/`):**
   - Verifique a saudação com data e hora e o botão `"Registrar Atividade"`.
2. **Reutilização do Header:**
   - Header exibindo: `"Monitoramento de Atividade Física - Formulário"`.
3. **Cálculo de Velocidade e Classificação:**
   - **Caminhada:** Distância = `4` km, Tempo = `60` min $\rightarrow$ Velocidade: `4.00 km/h`, Classificação: **Caminhada** ($\le 5$ km/h).
   - **Trote:** Distância = `8` km, Tempo = `60` min $\rightarrow$ Velocidade: `8.00 km/h`, Classificação: **Trote** ($5$ a $10$ km/h).
   - **Corrida:** Distância = `10` km, Tempo = `45` min $\rightarrow$ Velocidade: `13.33 km/h`, Classificação: **Corrida** ($> 10$ km/h).
4. **Tratamento de Casos Limite:**
   - Teste com Distância = `0` ou Tempo = `0`. O sistema bloqueia tanto no frontend quanto no backend com mensagem amigável `"Distância e tempo devem ser maiores que zero"`.
5. **Salvar no Banco:**
   - Clique em `"Salvar"`. A atividade é persistida via `POST /atividades` com: Distância, Tempo, Velocidade Média, Classificação e Data/Hora.
6. **Tabela de Registros:**
   - Tabela exibindo todas as 5 colunas exigidas no PDF 2.
7. **Limpar Tabela:**
   - Clique em `"Limpar Tabela"`. Todos os registros são apagados via `DELETE /atividades`.
