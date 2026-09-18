package com.simulado1.service;

import com.simulado1.dto.InvestimentoDTO;
import com.simulado1.model.Investimento;
import com.simulado1.repository.InvestimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository investimentoRepository;

    /**
     * Realiza o cálculo no backend sem persistir imediatamente:
     * ValorFinal = ValorInicial * (1 + JurosMensais / 100) ^ Prazo
     */
    public InvestimentoDTO.Response calcular(InvestimentoDTO.Request req) {
        double taxaDecimal = req.getJuroMensal() / 100.0;
        double montante = req.getValorInicial() * Math.pow(1.0 + taxaDecimal, req.getPrazoMeses());

        // Arredondamento comercial para 2 casas decimais
        BigDecimal bd = BigDecimal.valueOf(montante).setScale(2, RoundingMode.HALF_UP);
        double valorFinal = bd.doubleValue();

        InvestimentoDTO.Response res = new InvestimentoDTO.Response();
        res.setValorInicial(req.getValorInicial());
        res.setPrazoMeses(req.getPrazoMeses());
        res.setJuroMensal(req.getJuroMensal());
        res.setValorFinal(valorFinal);
        res.setMensagem(String.format("o valor final será de %.2f", valorFinal));
        return res;
    }

    public Investimento salvar(InvestimentoDTO.Request req) {
        double taxaDecimal = req.getJuroMensal() / 100.0;
        double montante = req.getValorInicial() * Math.pow(1.0 + taxaDecimal, req.getPrazoMeses());
        double valorFinal = BigDecimal.valueOf(montante).setScale(2, RoundingMode.HALF_UP).doubleValue();

        Investimento entidade = new Investimento();
        entidade.setValorInicial(req.getValorInicial());
        entidade.setPrazoMeses(req.getPrazoMeses());
        entidade.setJuroMensal(req.getJuroMensal());
        entidade.setValorFinal(valorFinal);
        entidade.setDataCalculo(LocalDate.now());

        return investimentoRepository.save(entidade);
    }

    public List<Investimento> listarTodos() {
        return investimentoRepository.findAll();
    }

    public List<Investimento> filtrarPorData(LocalDate data) {
        return investimentoRepository.findByDataCalculo(data);
    }

    public List<Investimento> filtrarPorPrazoOuJuro(String termo) {
        try {
            double val = Double.parseDouble(termo);
            return investimentoRepository.filtrarPorPrazoOuJuro((int) val, val);
        } catch (NumberFormatException e) {
            return listarTodos();
        }
    }

    public void limparTodos() {
        investimentoRepository.deleteAll();
    }
}
