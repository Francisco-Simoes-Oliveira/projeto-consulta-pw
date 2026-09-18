package com.simulado2.service;

import com.simulado2.dto.AtividadeDTO;
import com.simulado2.model.Atividade;
import com.simulado2.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository atividadeRepository;

    public AtividadeDTO.Response calcular(AtividadeDTO.Request req) {
        if (req.getTempo() <= 0 || req.getDistancia() <= 0) {
            throw new IllegalArgumentException("Distância e tempo devem ser maiores que zero.");
        }

        // Converte minutos para horas: tempo_h = tempo_min / 60
        double tempoHoras = req.getTempo() / 60.0;
        double velocidade = req.getDistancia() / tempoHoras;
        double velocidadeArredondada = BigDecimal.valueOf(velocidade).setScale(2, RoundingMode.HALF_UP).doubleValue();

        String classificacao = classificar(velocidadeArredondada);

        AtividadeDTO.Response res = new AtividadeDTO.Response();
        res.setDistancia(req.getDistancia());
        res.setTempo(req.getTempo());
        res.setVelocidadeMedia(velocidadeArredondada);
        res.setClassificacao(classificacao);
        return res;
    }

    public Atividade salvar(AtividadeDTO.Request req) {
        AtividadeDTO.Response calculo = calcular(req);

        Atividade entidade = new Atividade();
        entidade.setDistancia(calculo.getDistancia());
        entidade.setTempo(calculo.getTempo());
        entidade.setVelocidadeMedia(calculo.getVelocidadeMedia());
        entidade.setClassificacao(calculo.getClassificacao());
        entidade.setDataHora(LocalDateTime.now());

        return atividadeRepository.save(entidade);
    }

    public List<Atividade> listar() {
        return atividadeRepository.findAll();
    }

    public void apagarTodos() {
        atividadeRepository.deleteAll();
    }

    /**
     * Faixas de classificação especificadas no PDF 2:
     * - até 5 km/h       -> Caminhada
     * - de 5 a 10 km/h   -> Trote
     * - acima de 10 km/h -> Corrida
     */
    private String classificar(double velocidade) {
        if (velocidade <= 5.0) {
            return "Caminhada";
        } else if (velocidade <= 10.0) {
            return "Trote";
        } else {
            return "Corrida";
        }
    }
}
