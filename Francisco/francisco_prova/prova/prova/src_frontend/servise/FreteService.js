import BaseService from "./BaseService";

class FreteService extends BaseService {
    constructor(){
        super("/api/frete")
    }

    async calcular(dados, setErro){
        try{
            if (dados.kg == null || dados.km == null || dados.tipo == null) {
                setErro("Todos os campos devem estar prenchidos");
                return null;
            }
            const responce = this.api.post(`${this.endPoint}/calcular`, dados)
            return responce.data;
        }
        catch { setErro("Erro ao salvar verifique se os dados cumprem o requisito") }
      
    }
  
    async salvar(dados, setErro){
        try{
            if (dados.kg == null || dados.km == null || dados.tipo == null) {
                setErro("Todos os campos devem estar prenchidos");
                return;
            }
            this.insert(dados);
            
        }
        catch{setErro("Erro ao salvar verifique se os dados cumprem o requisito")}
    }

    async limparTabela(){
        this.api.delete(this.endPoint);
    }

    

}

export default FreteService;