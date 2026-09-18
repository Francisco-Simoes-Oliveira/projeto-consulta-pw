package com.recuperacao.backend.servise;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.recuperacao.backend.dto.FreteDtos.FreteResponse;
import com.recuperacao.backend.dto.FreteDtos.FreteCalculoResponse;
import com.recuperacao.backend.dto.FreteDtos.FreteRequest;
import com.recuperacao.backend.dto.FreteDtos.FreteSaveRequest;
import com.recuperacao.backend.enums.TipoEnvio;
import com.recuperacao.backend.model.Frete;
import com.recuperacao.backend.repository.FreteRepository;

/*Para frete econômico, o valor final é obtido por:
ValorFrete = (Peso × 3,50) + (Distância × 1,20)
Para frete expresso, aplique um adicional percentual sobre o valor econômico:
ValorFrete = [(Peso × 3,50) + (Distância × 1,20)] × (1 + (AdicionalUrgencia / 100))
 */


@Service
public class FreteService {
    @Autowired
    private FreteRepository repository;

    public FreteCalculoResponse  calcular(FreteRequest request){
       
        if (request.tipo() == TipoEnvio.ECONOMICO) {
            return new FreteCalculoResponse((request.kg() * 3.5) + (request.km()*1.2)) ;
        }else if (request.tipo() == TipoEnvio.EXPRESSO) {
            return new FreteCalculoResponse((((request.kg() * 3.5) + (request.km()*1.2) )* (1+ (request.adicDeUrgencia()/100))));
        }
        return null;
    }

    public void salvar(FreteSaveRequest request){
        Frete frete = new Frete();
        FreteCalculoResponse calculo = calcular(new FreteRequest(request.kg(), request.km(), request.adicDeUrgencia(), request.tipo()));

        frete.setKg(request.kg());
        frete.setKm(request.km());
        frete.setTipo(request.tipo());
        frete.setAdicDeUrgencia(request.adicDeUrgencia());
        frete.setValorFrete(calculo.valorFrete());
        frete.setCalculadoEm(LocalDateTime.now());
        repository.save(frete);
    }

    public List<Frete> listarAll(){
        return repository.findAll();
    }

    public void deletarId(Long id){
        repository.deleteById(id);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
