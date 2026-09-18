const extrairMensagemErro = (error, mensagemPadrao) => {
    const resposta = error.response;

    if (!resposta) {
        return "Não foi possível conectar ao servidor. Verifique se o backend está rodando na porta 8080.";
    }

    const dados = resposta.data;

    if (dados && Array.isArray(dados.erros) && dados.erros.length > 0) {
        return dados.erros.join(" | ");
    }

    if (dados && dados.mensagem) {
        return dados.mensagem;
    }

    return mensagemPadrao;
};

export default extrairMensagemErro;
