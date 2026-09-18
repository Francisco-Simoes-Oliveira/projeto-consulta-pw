export const formatarMoeda = (valor) => {
    if (valor === null || valor === undefined) {
        return "-";
    }
    return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
};

export const formatarDataHora = (dataHora) => {
    if (!dataHora) {
        return "-";
    }
    return new Date(dataHora).toLocaleString("pt-BR");
};

export const formatarPorcentagem = (valor) => {
    if (valor === null || valor === undefined) {
        return "-";
    }
    return `${valor.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 4 })}%`;
};

export const formatarTipoRendimento = (tipoRendimento) => {
    return tipoRendimento === "COMPOSTO" ? "Juros compostos" : "Juros simples";
};
