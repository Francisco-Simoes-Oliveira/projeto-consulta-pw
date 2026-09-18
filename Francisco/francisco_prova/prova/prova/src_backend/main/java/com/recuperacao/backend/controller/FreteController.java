package com.recuperacao.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recuperacao.backend.dto.FreteDtos.FreteCalculoResponse;
import com.recuperacao.backend.dto.FreteDtos.FreteRequest;
import com.recuperacao.backend.dto.FreteDtos.FreteSaveRequest;
import com.recuperacao.backend.model.Frete;
import com.recuperacao.backend.servise.FreteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/frete")
@CrossOrigin
public class FreteController {
    
    @Autowired
    FreteService service;

    @PostMapping("/calcular")
    public ResponseEntity<FreteCalculoResponse> calcular(@Valid @RequestBody  FreteRequest request){
        return ResponseEntity.ok(service.calcular(request));
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@Valid @RequestBody FreteSaveRequest request){
        service.salvar(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<Frete>> listarAll(){
        return ResponseEntity.ok(service.listarAll());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){
        service.deleteAll();
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteId(@PathVariable("id") Long id){
        service.deletarId(id);
        return ResponseEntity.ok(null);
    }

}
