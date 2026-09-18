# 04 — Relatórios

**Requisito 1.3.** Pelo menos um relatório. Aqui tem dois, que se completam:

1. **Resumo mensal por categoria** (JSON) — para mostrar na tela em cards/gráfico.
2. **Extrato em CSV** — botão "Exportar" que baixa um arquivo e abre no Excel.

## Passo a passo

1. Copie [RelatorioService.java](RelatorioService.java) e [RelatorioController.java](RelatorioController.java).
2. Adicione no `TransacaoRepository` as consultas que estão comentadas no fim do service.
3. Teste:

```
GET /api/relatorio/resumo?carteiraId=1&ano=2026&mes=11
GET /api/relatorio/extrato.csv?carteiraId=1&ano=2026&mes=11
```

## Detalhes que fazem diferença

- **A soma é feita pelo banco** (`sum`, `group by`), não em Java. Trazer 10 mil transações para somar
  no `for` é o erro clássico — e o professor pergunta isso.
- **Dinheiro é `BigDecimal`**, nunca `double`. Com `double`, `0.1 + 0.2` dá `0.30000000000000004`.
- O CSV sai com `;` (separador que o Excel em português entende) e com **BOM UTF-8**, senão acentos
  viram caracteres estranhos ao abrir no Excel.
- O relatório também respeita o controle de acesso: sempre filtre pela carteira do usuário logado
  (mesma ideia da pasta 03).

## Se quiser ir além

- PDF com a biblioteca **OpenPDF** ou **iText** (mesma lógica, só muda a forma de escrever o arquivo).
- Gráfico no front com **Chart.js** ou **Recharts** consumindo o JSON do resumo.
- Um `@Scheduled` mandando o resumo do mês por e-mail no dia 1º.
