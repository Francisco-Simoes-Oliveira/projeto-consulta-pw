package br.edu.ifpr.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpr.backend.service.AtividadeService;

@RestController
@RequestMapping("atividades")
@CrossOrigin
public class AtividadeController {
    @Autowired
    private AtividadeService service;
}
