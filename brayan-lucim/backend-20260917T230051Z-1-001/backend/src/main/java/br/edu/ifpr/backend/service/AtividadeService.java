package br.edu.ifpr.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpr.backend.enums.Classificacao;
import br.edu.ifpr.backend.model.Atividade;
import br.edu.ifpr.backend.repository.AtividadeRepository;

@Service
public class AtividadeService {
    
    @Autowired
    private AtividadeRepository repository;

    public void calcVelocidade(Atividade atividade){
        double tempoEmHoras = atividade.getTempo()/60.0;
        atividade.setVelocidadeMedia(atividade.getDistancia()/tempoEmHoras);
    }

    public void classificando(Atividade atividade){

        if(atividade.getVelocidadeMedia() <= 5) atividade.setClassificacao(Classificacao.CAMINHADA);

        else if (atividade.getVelocidadeMedia() <= 10) atividade.setClassificacao(Classificacao.TROTE);

        else{
            atividade.setClassificacao(Classificacao.CORRIDA);
        }
    }

    public Atividade create(Atividade atividade){
        calcVelocidade(atividade);
        classificando(atividade);
        atividade.setDataHora(LocalDateTime.now());
        return repository.save(atividade);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public List<Atividade> findAll(){
        return repository.findAll();
    }
}
