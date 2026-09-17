# Pacote de consulta — Programação Web

Arquivos:

- `CONSULTA_DEFINITIVA.md`: guia principal de prova.
- `DEPLOY_E_IA.md`: deploy, variáveis e integração com IA.
- `PROJETO_BASE/`: esqueleto genérico React + Spring Boot.

O projeto-base usa uma entidade simples chamada `Item`. A ideia é copiar/renomear a feature durante a prova.

## Estratégia na prova

1. Abra `CONSULTA_DEFINITIVA.md` e procure pela palavra-chave da questão.
2. Teste backend primeiro no Swagger.
3. Só depois conecte o React.
4. Para uma entidade nova, copie o padrão `Item` e altere campos.
5. Para endpoint protegido, use o interceptor Axios já pronto.
6. Para tempo real, publique evento após `save()` e faça subscribe no React.
