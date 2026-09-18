package com.prova.back.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prova.back.dto.DataHoraDTO;
import com.prova.back.dto.ResultadoCalculoDTO;
import com.prova.back.dto.ResumoDTO;
import com.prova.back.exception.NaoEncontradoExcecao;
import com.prova.back.exception.NegocioExcecao;
import com.prova.back.models.Investimento;
import com.prova.back.models.TipoRendimento;
import com.prova.back.repository.InvestimentoRepository;

@Service
public class InvestimentoService {

    private static final DateTimeFormatter FORMATO_BRASILEIRO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final LocalDateTime DATA_MINIMA = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime DATA_MAXIMA = LocalDateTime.of(2999, 12, 31, 23, 59, 59);
    private static final int PRAZO_MINIMO = 1;
    private static final int PRAZO_MAXIMO = 600;
    private static final double TAXA_MINIMA = 0.0;
    private static final double TAXA_MAXIMA = 100.0;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    public ResultadoCalculoDTO calcular(Investimento investimento) {
        double valorFinal = calcularValorFinal(investimento);
        return new ResultadoCalculoDTO(
                investimento.getValorInicial(),
                investimento.getPrazoMeses(),
                investimento.getTaxaMensal(),
                investimento.getTipoRendimento(),
                valorFinal,
                arredondar(valorFinal - investimento.getValorInicial()));
    }

    public Investimento create(Investimento investimento) {
        investimento.setId(null);
        investimento.setValorFinal(calcularValorFinal(investimento));
        investimento.setDataCalculo(LocalDateTime.now());
        return investimentoRepository.save(investimento);
    }

    public Investimento update(Long id, Investimento investimento) {
        Investimento investimentoSalvo = buscarPorId(id);
        investimentoSalvo.setValorInicial(investimento.getValorInicial());
        investimentoSalvo.setPrazoMeses(investimento.getPrazoMeses());
        investimentoSalvo.setTaxaMensal(investimento.getTaxaMensal());
        investimentoSalvo.setTipoRendimento(investimento.getTipoRendimento());
        investimentoSalvo.setValorFinal(calcularValorFinal(investimentoSalvo));
        investimentoSalvo.setDataCalculo(LocalDateTime.now());
        return investimentoRepository.save(investimentoSalvo);
    }

    public void delete(Long id) {
        investimentoRepository.delete(buscarPorId(id));
    }

    public void deleteAll() {
        investimentoRepository.deleteAll();
    }

    public List<Investimento> listar(LocalDate dataInicio, LocalDate dataFim, Integer prazoMinimo,
            Integer prazoMaximo, Double taxaMinima, Double taxaMaxima) {

        LocalDateTime inicio = dataInicio == null ? DATA_MINIMA : dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim == null ? DATA_MAXIMA : dataFim.atTime(LocalTime.MAX);
        int prazoDe = prazoMinimo == null ? PRAZO_MINIMO : prazoMinimo;
        int prazoAte = prazoMaximo == null ? PRAZO_MAXIMO : prazoMaximo;
        double taxaDe = taxaMinima == null ? TAXA_MINIMA : taxaMinima;
        double taxaAte = taxaMaxima == null ? TAXA_MAXIMA : taxaMaxima;

        if (inicio.isAfter(fim)) {
            throw new NegocioExcecao("A data inicial não pode ser maior que a data final");
        }
        if (prazoDe > prazoAte) {
            throw new NegocioExcecao("O prazo inicial não pode ser maior que o prazo final");
        }
        if (taxaDe > taxaAte) {
            throw new NegocioExcecao("A taxa inicial não pode ser maior que a taxa final");
        }

        return investimentoRepository
                .findByDataCalculoBetweenAndPrazoMesesBetweenAndTaxaMensalBetweenOrderByDataCalculoDesc(
                        inicio, fim, prazoDe, prazoAte, taxaDe, taxaAte);
    }

    public ResumoDTO resumo() {
        Double valorFinalMedio = investimentoRepository.calcularValorFinalMedio();
        return new ResumoDTO(
                investimentoRepository.count(),
                valorFinalMedio == null ? 0.0 : arredondar(valorFinalMedio),
                investimentoRepository.buscarDataUltimaSimulacao());
    }

    public DataHoraDTO dataHoraAtual() {
        LocalDateTime agora = LocalDateTime.now();
        return new DataHoraDTO(agora, agora.format(FORMATO_BRASILEIRO));
    }

    private Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoExcecao("Investimento não encontrado com o id " + id));
    }

    private double calcularValorFinal(Investimento investimento) {
        validar(investimento);
        double taxa = investimento.getTaxaMensal() / 100.0;
        double valorFinal;
        if (investimento.getTipoRendimento() == TipoRendimento.COMPOSTO) {
            valorFinal = investimento.getValorInicial() * Math.pow(1 + taxa, investimento.getPrazoMeses());
        } else {
            valorFinal = investimento.getValorInicial() * (1 + taxa * investimento.getPrazoMeses());
        }
        return arredondar(valorFinal);
    }

    private void validar(Investimento investimento) {
        if (investimento.getValorInicial() == null || investimento.getValorInicial() <= 0) {
            throw new NegocioExcecao("O valor inicial precisa ser maior que zero");
        }
        if (investimento.getPrazoMeses() == null || investimento.getPrazoMeses() < PRAZO_MINIMO
                || investimento.getPrazoMeses() > PRAZO_MAXIMO) {
            throw new NegocioExcecao("O prazo precisa estar entre 1 e 600 meses");
        }
        if (investimento.getTaxaMensal() == null || investimento.getTaxaMensal() < TAXA_MINIMA
                || investimento.getTaxaMensal() > TAXA_MAXIMA) {
            throw new NegocioExcecao("A taxa mensal precisa estar entre 0 e 100");
        }
        if (investimento.getTipoRendimento() == null) {
            throw new NegocioExcecao("Informe o tipo de rendimento: SIMPLES ou COMPOSTO");
        }
    }

    private double arredondar(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
