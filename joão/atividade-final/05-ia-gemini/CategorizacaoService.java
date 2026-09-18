package com.financas.backend.ia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financas.backend.dto.ResumoCategoriaDTO;

/**
 * Onde a IA vira funcionalidade de verdade.
 *
 * Duas regras importantes:
 * 1) a resposta da IA é sempre CONFERIDA antes de virar dado do sistema;
 * 2) se a IA falhar, o sistema continua funcionando (cai no padrão).
 */
@Service
public class CategorizacaoService {

    private static final List<String> CATEGORIAS = List.of(
            "ALIMENTACAO", "TRANSPORTE", "MORADIA", "SAUDE", "EDUCACAO",
            "LAZER", "COMPRAS", "SALARIO", "INVESTIMENTO", "OUTROS");

    private static final String PADRAO = "OUTROS";

    @Autowired
    private GeminiClient geminiClient;

    /**
     * "Uber para o trabalho" -> TRANSPORTE
     */
    public String sugerirCategoria(String descricao) {
        String prompt = """
                Classifique a despesa abaixo em UMA das categorias desta lista:
                %s

                Responda apenas com o nome da categoria, em maiúsculas, sem explicação.
                Se não tiver certeza, responda OUTROS.

                Descrição: "%s"
                """.formatted(String.join(", ", CATEGORIAS), descricao);

        try {
            String resposta = geminiClient.perguntar(prompt)
                    .toUpperCase()
                    .replaceAll("[^A-Z]", ""); // limpa pontuação, aspas e quebras de linha

            // a IA pode inventar uma categoria: só aceitamos o que está na lista
            return CATEGORIAS.contains(resposta) ? resposta : PADRAO;
        } catch (Exception erro) {
            // IA fora do ar, chave errada, timeout... a transação é salva assim mesmo
            return PADRAO;
        }
    }

    /**
     * Transforma os números do relatório (pasta 04) em um texto curto de análise.
     */
    public String analisarGastos(List<ResumoCategoriaDTO> resumo) {
        if (resumo.isEmpty()) {
            return "Ainda não há despesas registradas neste mês.";
        }

        StringBuilder dados = new StringBuilder();
        for (ResumoCategoriaDTO linha : resumo) {
            dados.append("- ").append(linha.getCategoria())
                    .append(": R$ ").append(linha.getTotal())
                    .append(" (").append(linha.getPercentual()).append("%)\n");
        }

        String prompt = """
                Você é um assistente financeiro. Analise os gastos do mês abaixo e escreva
                no máximo 4 frases, em português do Brasil, em tom simples e direto:
                aponte a maior categoria, algo que chame atenção e uma sugestão prática.
                Não invente números que não estejam na lista.

                %s
                """.formatted(dados);

        try {
            return geminiClient.perguntar(prompt);
        } catch (Exception erro) {
            return "Não foi possível gerar a análise agora. Tente novamente em instantes.";
        }
    }
}
