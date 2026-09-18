# PW Gestão Empresarial
Modelo full stack React + Spring Boot para estudo/prova.

## Perfis prontos
- ADMIN: `admin@teste.com` / `123456`
- USER: `usuario@teste.com` / `123456`

## Módulos
Dashboard, produtos, estoque, vendas, financeiro e administração de usuários.
A venda é transacional: valida estoque, baixa quantidade, cria histórico e lança receita.

## Rodar
Backend: `cd backend` e `mvnw.cmd spring-boot:run` (Windows) ou `./mvnw spring-boot:run`.
Frontend: `cd frontend`, `npm install`, `npm run dev`.
Swagger: http://localhost:8080/swagger-ui.html
H2: http://localhost:8080/h2-console
