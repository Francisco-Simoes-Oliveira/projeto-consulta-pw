import BaseService from './BaseService';

class AtividadeService extends BaseService {
  constructor() {
    super('/atividades');
  }

  async calcular(dados) {
    const resposta = await this.api.post(`${this.endPoint}/calcular`, dados);
    return resposta.data;
  }
}

export default new AtividadeService();
