import api from './api'
export default class BaseService {
  constructor(endpoint){ this.endpoint=endpoint }
  async list(){ return (await api.get(this.endpoint)).data }
  async get(id){ return (await api.get(`${this.endpoint}/${id}`)).data }
  async create(data){ return (await api.post(this.endpoint,data)).data }
  async update(id,data){ return (await api.put(`${this.endpoint}/${id}`,data)).data }
  async remove(id){ return api.delete(`${this.endpoint}/${id}`) }
}
