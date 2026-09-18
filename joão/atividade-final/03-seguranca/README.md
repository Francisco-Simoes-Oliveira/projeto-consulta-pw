# 03 — Segurança (OWASP Top 10)

**Requisito 1.6.** Não precisa cobrir a lista inteira: escolha os riscos que fazem sentido para um
sistema financeiro compartilhado e saiba explicar cada escolha na apresentação.

## O que cada arquivo resolve

| Risco (OWASP) | Como está resolvido aqui | Arquivo |
|---|---|---|
| A01 – Broken Access Control | Toda consulta filtra pelo usuário logado; ninguém abre carteira dos outros | [CarteiraService.java](CarteiraService.java) |
| A02 – Falhas de criptografia | Senha salva com BCrypt, nunca em texto puro | [ConfiguracaoSeguranca.java](ConfiguracaoSeguranca.java) |
| A03 – Injeção (SQL) | JPA com parâmetros (`:email`), nunca concatenando string | [CarteiraService.java](CarteiraService.java) |
| A03 – XSS | React já escapa o que renderiza + validação/limpeza na entrada | [AutenticacaoController.java](AutenticacaoController.java) |
| A05 – Configuração insegura | Cabeçalhos de segurança (CSP, HSTS, frame-options) e CORS restrito | [ConfiguracaoSeguranca.java](ConfiguracaoSeguranca.java) |
| A07 – Falhas de autenticação | Login com JWT + limite de tentativas | [JwtService.java](JwtService.java), [FiltroRateLimit.java](FiltroRateLimit.java) |
| Segredos no repositório | Chave e senha vêm de variáveis de ambiente | [application-exemplo.properties](application-exemplo.properties) |

## Passo a passo

1. Dependências: `spring-boot-starter-security` e as três do `jjwt` (veja o README da pasta raiz).
2. Copie as classes para `config/`, `security/` e `controller/`.
3. Crie as variáveis de ambiente antes de rodar:

```bash
set JWT_SECRET=uma-chave-bem-grande-com-no-minimo-32-caracteres
set DB_PASSWORD=sua_senha_do_banco
```

No Linux/Mac troque `set` por `export`. No deploy (Render, Railway…) essas variáveis são cadastradas
no painel do serviço.

4. Teste: `POST /api/auth/login` devolve o token; as outras rotas só respondem com o header
   `Authorization: Bearer <token>`.

## Sobre CSRF

O CSRF está **desligado** de propósito, e isso precisa ser justificado na apresentação:

- O ataque CSRF depende do navegador mandar credencial sozinho — o que acontece com **cookie de sessão**.
- Aqui a API é *stateless* e o token vai num header `Authorization`, que o navegador **não** manda
  automaticamente para outro site. Logo, não há o que roubar por CSRF.
- Se você guardar o JWT em cookie, aí sim **precisa** ligar o CSRF (e usar `HttpOnly`, `Secure`, `SameSite=Strict`).

## Pequenas coisas que valem ponto

- Nunca devolva a entidade `Usuario` no JSON (vai a senha junto): use um DTO.
- Mensagem de erro de login genérica ("E-mail ou senha inválidos") — não diga qual dos dois errou.
- `.gitignore` com `application-secrets.properties` e `.env`.
- Se algum segredo já foi commitado, **troque o segredo**: apagar o arquivo não apaga o histórico.
