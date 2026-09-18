package com.ifpr.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpr.backend.dto.ResumoInvestimentoDTO;
import com.ifpr.backend.model.Investimento;
import com.ifpr.backend.service.InvestimentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/investimento")
@CrossOrigin
public class InvestimentoController {
    
    @Autowired
    private InvestimentoService investimentoService;

    @PostMapping("/calcular")
    public Investimento calcular(@RequestBody @Valid Investimento investimento) {
        return investimentoService.calcular(investimento);
    }

    @PostMapping
    public Investimento create(@RequestBody @Valid Investimento investimento) {
        return investimentoService.create(investimento);
    }

    @GetMapping("/resumo")
    public ResumoInvestimentoDTO resumo(){
        return investimentoService.resumo();
    }

    @GetMapping("/filtro/data")
    public List<Investimento> findByData(@RequestParam LocalDateTime dataInicial, @RequestParam LocalDateTime dataFinal){
        return investimentoService.findByData(dataInicial, dataFinal);
    }

    @GetMapping("/filtro/prazo")
    public List<Investimento> findByPrazo(@RequestParam Integer prazoInicial, @RequestParam Integer prazoFinal){
        return investimentoService.findByPrazo(prazoInicial, prazoFinal);
    }

    @GetMapping
    public List<Investimento> findAll(){
        return investimentoService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        investimentoService.delete(id);
    }


    @DeleteMapping
    public void deleteAll(){
        investimentoService.deleteAll();
    }

}
