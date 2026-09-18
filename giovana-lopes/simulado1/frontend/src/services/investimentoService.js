import BaseService from './BaseService';

class InvestimentoService extends BaseService {
  constructor() {
    super('/api/investimentos');
  }

  async calcular(dados) {
    const resposta = await this.api.post(`${this.endPoint}/calcular`, dados);
    return resposta.data;
  }

  async filtrarPorData(dataIso) {
    const resposta = await this.api.get(`${this.endPoint}/filtrar/data`, {
      params: { data: dataIso }
    });
    return resposta.data;
  }

  async filtrarPorTermo(termo) {
    const resposta = await this.api.get(`${this.endPoint}/filtrar/termo`, {
      params: { termo }
    });
    return resposta.data;
  }
}

export default new InvestimentoService();
