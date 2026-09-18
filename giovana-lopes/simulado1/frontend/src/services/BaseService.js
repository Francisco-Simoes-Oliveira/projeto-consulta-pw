import api from '../config/axiosConfig';

class BaseService {
  constructor(endPoint) {
    this.endPoint = endPoint;
    this.api = api;
  }

  async inserir(dados) {
    const resposta = await this.api.post(this.endPoint, dados);
    return resposta.data;
  }

  async alterar(dados) {
    const resposta = await this.api.put(this.endPoint, dados);
    return resposta.data;
  }

  async excluir(id) {
    const resposta = await this.api.delete(`${this.endPoint}/${id}`);
    return resposta.data;
  }

  async buscarTodos() {
    const resposta = await this.api.get(this.endPoint);
    return resposta.data;
  }

  async excluirTodos() {
    const resposta = await this.api.delete(this.endPoint);
    return resposta.data;
  }
}

export default BaseService;
