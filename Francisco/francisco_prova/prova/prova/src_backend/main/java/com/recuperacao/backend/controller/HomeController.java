package com.recuperacao.backend.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recuperacao.backend.dto.HomeDtos.HomeResponse;
import com.recuperacao.backend.servise.HomeServise;

@RestController
@RequestMapping("/api/home")
@CrossOrigin
public class HomeController {

    @Autowired
    HomeServise servise;

    @GetMapping
    public ResponseEntity<HomeResponse> getAgora(){
        return ResponseEntity.ok(servise.getAgora());
    }
}
