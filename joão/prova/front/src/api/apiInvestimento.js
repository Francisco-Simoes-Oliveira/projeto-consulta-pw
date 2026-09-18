import api from "./api";

class InvestimentoService {

    constructor() {
        this.api = api;
        this.endPoint = "investimento";
    }

    async calcular(investimento) {
        const response = await this.api.post(`${this.endPoint}/calcular`, investimento);
        return response.data;
    }

    async salvar(investimento) {
        const response = await this.api.post(this.endPoint, investimento);
        return response.data;
    }

    async listar(filtros) {
        const response = await this.api.get(this.endPoint, { params: filtros });
        return response.data;
    }

    async excluir(id) {
        await this.api.delete(`${this.endPoint}/${id}`);
    }

    async excluirTodos() {
        await this.api.delete(this.endPoint);
    }

    async buscarResumo() {
        const response = await this.api.get(`${this.endPoint}/resumo`);
        return response.data;
    }

    async buscarDataHora() {
        const response = await this.api.get(`${this.endPoint}/data-hora`);
        return response.data;
    }
}

export default InvestimentoService;
