import BaseService from "./BaseService";

class InvestimentoService extends BaseService {
    constructor() {
        super('investimento');
    }

    async calcular(credentials) {
        const response = await
            this.api.post(`${this.endPoint}/calcular`, credentials);
        return response.data;
    }

    async resumo() {
        const response = await
            this.api.get(`${this.endPoint}/resumo`);
        return response.data;
    }

    async deleteAll() {
        const response = await
            this.api.delete(this.endPoint);
        return response.data;
    }

    async filtrarPorData(dataInicial, dataFinal){
        const response = await this.api.get(`${this.endPoint}/filtro/data`,
                {
                    params: {
                        dataInicial,
                        dataFinal
                    }
                }
            );
        return response.data

    }

    async filtrarPorPrazo(prazoInicial, prazoFinal) {
        const response = await this.api.get(`${this.endPoint}/filtro/prazo`,
            {
                params: {
                    prazoInicial,
                    prazoFinal
                }
            }
        );
        return response.data;

    }

}

export default InvestimentoService;