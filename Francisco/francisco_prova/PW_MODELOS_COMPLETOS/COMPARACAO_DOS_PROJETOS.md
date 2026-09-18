# Comparação rápida

| Projeto | USER | ADMIN | Regra de negócio principal |
|---|---|---|---|
| Gestão Empresarial | produtos, vendas próprias | estoque, financeiro, relatórios, usuários | venda baixa estoque e lança receita |
| Rede Social | feed, post, like, comentário | moderação e usuários | autor/permissão vem da autenticação |
| Loja Online | catálogo, carrinho, checkout, pedidos próprios | produtos, todos pedidos, status, usuários | checkout baixa estoque; cancelamento repõe |

Os três foram feitos para funcionar como modelos independentes de prova, mantendo a mesma base de autenticação e organização para facilitar comparação e reaproveitamento de código.
