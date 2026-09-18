package com.recuperacao.backend.dto;



public class HomeDtos {
    public record HomeResponse(
        String data,
        Long qtdeSimu
        // double valMedio

    )
    {}
    
}