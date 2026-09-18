package com.ifpr.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifpr.backend.model.Investimento;
import com.ifpr.backend.repository.InvestimentoRepository;
import com.ifpr.backend.dto.ResumoInvestimentoDTO;

@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository investimentoRepository;

    public Investimento calcular(Investimento investimento){

        Double valorFinal;
        if("SIMPLES".equalsIgnoreCase(investimento.getTipoRendimento())){
            valorFinal = investimento.getValorInicial()*
            (1 + (investimento.getTaxaJuros() / 100) *
            investimento.getPrazoMeses());
        } else {
            valorFinal = investimento.getValorInicial() *
            Math.pow(1 + investimento.getTaxaJuros() / 100, investimento.getPrazoMeses());
        }
        investimento.setValorFinal(valorFinal);

        return investimento;
    }

    public Investimento create(Investimento investimento){
        calcular(investimento);
        investimento.setDataCalculo(LocalDateTime.now());
        return investimentoRepository.save(investimento);
    }

    public List<Investimento> findAll(){
        return investimentoRepository.findAll();
    }

    public List<Investimento> findByData(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return investimentoRepository.findByDataCalculoBetween(dataInicial, dataFinal);
    }

    public List<Investimento> findByPrazo(Integer prazoInicial, Integer prazoFinal){
        return investimentoRepository.findByPrazoMesesBetween(prazoInicial, prazoFinal);
    }

    public void delete(Long id) {
        investimentoRepository.deleteById(id);
    }

    public void deleteAll(){
        investimentoRepository.deleteAll();
    }

    public ResumoInvestimentoDTO resumo(){
        List<Investimento> investimentos = investimentoRepository.findAll();

        ResumoInvestimentoDTO resumo = new ResumoInvestimentoDTO();

        resumo.setDataHora(LocalDateTime.now());
        resumo.setQuantidadeSimulacoes(investimentos.size());

        Double valorFinalMedio = investimentos.stream()
        .mapToDouble(Investimento::getValorFinal)
        .average()
        .orElse(0.0);

        resumo.setValorFinalMedio(valorFinalMedio);

        LocalDateTime ultimaSimulacao = investimentos.stream()
        .map(Investimento::getDataCalculo)
        .max(LocalDateTime::compareTo)
        .orElse(null);

        resumo.setUltimaSimulacao(ultimaSimulacao);

        return resumo;
    }
}
