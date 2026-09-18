package com.recuperacao.backend.servise;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.recuperacao.backend.dto.HomeDtos.HomeResponse;
import com.recuperacao.backend.model.Frete;
import com.recuperacao.backend.repository.FreteRepository;

@Service
public class HomeServise {

    @Autowired
    private FreteRepository repository;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

    public HomeResponse getAgora(){
        String data = LocalDateTime.now().format(formatter);
        Long qtdeSimu = repository.count();
        // double valMedio = repository.media();
        
        
        return new HomeResponse(data, qtdeSimu);
    }



}
