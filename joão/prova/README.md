# Simulador de Investimentos — React + Spring Boot

MVP de uma calculadora de investimentos: o usuário informa valor inicial, prazo, taxa mensal e o
tipo de rendimento (juros simples ou compostos), o **backend** calcula, guarda o histórico em banco
e o **frontend** mostra o resultado, o histórico e os filtros.

```
back/                     -> API REST (Spring Boot 3.5 + JPA + MySQL)
front/                    -> Aplicação React (Create React App, JavaScript puro)
README.md                 -> este arquivo
```

---

## 1. Pré-requisitos

| Ferramenta | Versão | Observação |
|---|---|---|
| JDK | 17 ou superior | `java -version` |
| Node.js | 18 ou superior | `node -v` (vem com o npm) |
| MySQL ou MariaDB | 5.7+ / 10+ | XAMPP, Laragon, Docker ou instalação normal |

Não é necessário instalar o Maven: o projeto tem o wrapper (`mvnw` / `mvnw.cmd`).

---

## 2. Criar o banco de dados

A URL de conexão já vem com `createDatabaseIfNotExist=true`, ou seja, **na primeira execução o
próprio Spring cria o banco `prova`**. As tabelas também são criadas sozinhas
(`spring.jpa.hibernate.ddl-auto=update`).

Se preferir criar na mão (ou se o seu usuário do MySQL não tiver permissão para criar bancos), rode:

```sql
CREATE DATABASE prova CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

Como criar, dependendo do ambiente:

- **XAMPP / Laragon**: abra o phpMyAdmin (`http://localhost/phpmyadmin`) → aba SQL → cole o comando acima.
- **MySQL Workbench**: abra uma query e execute o comando acima.
- **Terminal**: `mysql -u root -p -e "CREATE DATABASE prova;"`
- **Docker** (se não tiver MySQL instalado):

```bash
docker run --name mysql-prova -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=prova -p 3306:3306 -d mysql:8
```

Não é preciso criar a tabela: o Hibernate cria a tabela `investimentos` sozinho, com as colunas
`id`, `valor_inicial`, `prazo_meses`, `taxa_mensal`, `tipo_rendimento`, `valor_final` e `data_calculo`.

---

## 3. O que colocar no `application.properties`

Arquivo: `back/src/main/resources/application.properties`

```properties
spring.application.name=back

# Banco de dados MySQL - ajuste porta, usuário e senha conforme o seu ambiente
spring.datasource.url=jdbc:mysql://localhost:3306/prova?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Criação e atualização automática das tabelas
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

server.port=8080
```

O que cada coisa significa e o que você precisa mudar:

| Propriedade | Para que serve | O que ajustar |
|---|---|---|
| `spring.datasource.url` | Endereço do banco | Troque `3306` se o seu MySQL usa outra porta (a configuração anterior deste projeto usava `5244`) e `prova` se quiser outro nome de banco |
| `spring.datasource.username` | Usuário do banco | Normalmente `root` |
| `spring.datasource.password` | Senha do banco | **Vazio** no XAMPP/Laragon; preencha se o seu MySQL tem senha (ex.: `root`) |
| `spring.jpa.hibernate.ddl-auto` | Criação das tabelas | `update` mantém os dados; use `create-drop` só se quiser apagar tudo a cada start |
| `server.port` | Porta da API | Se mudar de `8080`, ajuste também a `baseURL` em `front/src/api/api.js` |

Parâmetros da URL, para não travar na hora da apresentação:

- `createDatabaseIfNotExist=true` → cria o banco se ele não existir;
- `useSSL=false` → evita o aviso de SSL em ambiente local;
- `allowPublicKeyRetrieval=true` → necessário no MySQL 8 com senha;
- `serverTimezone=America/Sao_Paulo` → evita erro de fuso horário na data/hora.

---

## 4. Como rodar

### Backend (porta 8080)

```bash
cd back
./mvnw spring-boot:run
```

No Windows (PowerShell/CMD):

```bash
cd back
mvnw.cmd spring-boot:run
```

Para gerar o `.jar`: `mvnw.cmd clean package` e depois `java -jar target/back-0.0.1-SNAPSHOT.jar`.

Teste rápido no navegador: <http://localhost:8080/api/investimento/data-hora>

### Frontend (porta 3000)

```bash
cd front
npm install
npm start
```

A aplicação abre em <http://localhost:3000>. **O backend precisa estar rodando antes**, senão a Home
mostra a mensagem de erro de conexão.

---

## 5. API REST

Base: `http://localhost:8080/api/investimento`

| Verbo | Rota | O que faz |
|---|---|---|
| `POST` | `/calcular` | Calcula o valor final **sem salvar** (botão Calcular) |
| `POST` | `/` | Calcula e salva a simulação (botão Salvar) — retorna `201` |
| `PUT` | `/{id}` | Atualiza uma simulação e recalcula o valor final |
| `GET` | `/` | Lista o histórico; aceita os filtros `dataInicio`, `dataFim`, `prazoMinimo`, `prazoMaximo`, `taxaMinima`, `taxaMaxima` |
| `DELETE` | `/{id}` | Exclui uma simulação — retorna `204` |
| `DELETE` | `/` | Limpa toda a tabela — retorna `204` |
| `GET` | `/resumo` | Quantidade de simulações, valor final médio e data da última simulação |
| `GET` | `/data-hora` | Data e hora **do servidor** (usada na Home) |

Exemplo de corpo (POST):

```json
{
  "valorInicial": 1000,
  "prazoMeses": 12,
  "taxaMensal": 1,
  "tipoRendimento": "COMPOSTO"
}
```

Exemplo de filtro: `GET /api/investimento?dataInicio=2026-09-01&dataFim=2026-09-17&prazoMinimo=12&taxaMaxima=2`

### Erros

Todos os erros voltam no mesmo formato, tratados no `ExcecaoGlobal`:

```json
{
  "status": 400,
  "titulo": "Erro de validação",
  "mensagem": "Verifique os campos informados",
  "erros": ["O valor inicial precisa ser maior que zero"]
}
```

- `400` — campos inválidos (Bean Validation);
- `404` — id inexistente;
- `422` — regra de negócio (ex.: data inicial maior que a final);
- `500` — erro inesperado.

---

## 6. Estrutura e responsabilidades

### Backend (`back/src/main/java/com/prova/back`)

| Pacote / classe | Responsabilidade |
|---|---|
| `models/Investimento` | Entidade JPA + validações (`@Positive`, `@Min`, `@Max`, `@DecimalMin`, `@DecimalMax`) |
| `models/TipoRendimento` | Enum `SIMPLES` / `COMPOSTO` |
| `repository/InvestimentoRepository` | Acesso ao banco, consulta com filtros e agregações (média e última data) |
| `service/InvestimentoService` | **Toda a regra de negócio**: fórmulas, validações, resumo, data/hora |
| `controller/InvestimentoController` | Apenas recebe a requisição e devolve a resposta (`@CrossOrigin` libera o React) |
| `dto/` | Objetos de resposta (`ResultadoCalculoDTO`, `ResumoDTO`, `DataHoraDTO`, `RespostaErro`) |
| `exception/` | `NegocioExcecao`, `NaoEncontradoExcecao` e o `@RestControllerAdvice` global |

Fórmulas (no service, nunca no frontend):

- Juros compostos: `ValorFinal = ValorInicial × (1 + TaxaMensal/100) ^ Prazo`
- Juros simples: `ValorFinal = ValorInicial × (1 + (TaxaMensal/100) × Prazo)`

### Frontend (`front/src`)

| Arquivo | Responsabilidade |
|---|---|
| `api/api.js` | Instância do axios com a `baseURL` da API |
| `api/apiInvestimento.js` | `InvestimentoService`: um método por endpoint |
| `api/erro.js` | Transforma o erro do backend em mensagem amigável |
| `components/Header.js` | **Componente único de cabeçalho**, recebe o título por prop |
| `components/PadraoLayout.js` | Layout padrão que repassa o título ao Header |
| `components/Alerta.js` | Faixa de mensagem de sucesso ou de erro, reaproveitada nas duas telas |
| `components/ModalConfirmacao.js` | Modal de confirmação usado na exclusão e no "Limpar tabela" |
| `pages/Home/Home.js` | Saudação com data/hora do backend + resumo agregado + botão para o simulador |
| `pages/Calculo/Calculo.js` | Formulário, resultado, filtros e tabela de histórico |
| `utils/formato.js` | Formatação de moeda, data, porcentagem (apresentação, não regra de negócio) |

---

## 7. Checklist da avaliação

- [x] Home com data/hora vinda do backend (`GET /data-hora`, sem `new Date()` no frontend)
- [x] Resumo agregado calculado no backend (`GET /resumo`)
- [x] Botão de acesso à simulação
- [x] Header único reaproveitado nas duas telas, com o texto "Listagem" recebido por prop
- [x] Inputs de valor inicial, prazo, taxa e tipo de rendimento
- [x] Cálculo 100% no backend (juros simples e compostos)
- [x] Botão Calcular sem recarregar a página
- [x] Botão Salvar persistindo no banco e atualizando a tabela
- [x] Exclusão individual por linha (com confirmação)
- [x] Limpar tabela com confirmação antes de executar
- [x] Tabela com Data do Cálculo, Tipo de Rendimento, Prazo, Juro Mensal e Valor Final, carregada do backend
- [x] Filtro por intervalo de datas e por faixa de prazo/taxa, resolvidos no backend
- [x] Validações (valor > 0, prazo 1–600, taxa 0–100) com mensagens de erro
- [x] Camadas controller / service / repository, sem regra de negócio no controller
- [x] Verbos HTTP coerentes e códigos de status apropriados

---

## 8. Roteiro para apresentar

1. Suba o MySQL, o backend e o frontend (seções 2 e 4).
2. Abra a Home e mostre a data/hora vindo da API (dá para abrir o DevTools na aba Network).
3. Clique em "Realizar cálculo de investimento".
4. Faça uma simulação com juros compostos (ex.: 1000 / 12 meses / 1% → R$ 1.126,83) e clique em Calcular.
5. Troque para juros simples (mesmos valores → R$ 1.120,00) para mostrar a diferença.
6. Clique em Salvar e mostre a linha aparecendo na tabela.
7. Use os filtros de data e de prazo/taxa (mostre na aba Network que o filtro vai como parâmetro para o backend).
8. Exclua uma linha e depois use "Limpar tabela" para mostrar a confirmação.
9. Volte para a Home e mostre o resumo atualizado.
10. Tente calcular com campos vazios ou fora do intervalo para mostrar as mensagens de validação.

---

## 9. Problemas comuns

| Sintoma | Causa provável | Solução |
|---|---|---|
| Home mostra "Não foi possível conectar ao servidor" | Backend não está rodando | Suba o backend na porta 8080 |
| `Access denied for user 'root'@'localhost'` | Senha errada | Ajuste `spring.datasource.password` |
| `Unknown database 'prova'` | Usuário sem permissão de criar banco | Crie o banco manualmente (seção 2) |
| `Public Key Retrieval is not allowed` | MySQL 8 com senha | Mantenha `allowPublicKeyRetrieval=true` na URL |
| `Communications link failure` | MySQL parado ou porta diferente | Inicie o MySQL e confira a porta na URL |
| Erro de CORS no navegador | Controller sem `@CrossOrigin` | Já está configurado no `InvestimentoController` |
| `Port 8080 was already in use` | Outra aplicação na porta | Feche a outra aplicação ou mude `server.port` (e a `baseURL` do front) |
| A porta 3000 já está em uso | Outro `npm start` aberto | Aceite a sugestão de usar outra porta ou feche o processo |
| `npm start` reclama de módulos | Dependências não instaladas | Rode `npm install` dentro de `front` |
