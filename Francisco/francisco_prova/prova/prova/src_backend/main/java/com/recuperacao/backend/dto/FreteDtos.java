package com.recuperacao.backend.dto;



import com.recuperacao.backend.enums.TipoEnvio;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FreteDtos {
    public record FreteRequest(

        @NotNull(message = "O peso e obrigatorio")
        @Min(0) 
        double kg,

        @NotNull
        @Positive 
        @Min(1) 
        @Max(5000) 
        double km,
        
        @Min(0) 
        @Max(100) 
        double adicDeUrgencia,

        @NotNull
        TipoEnvio tipo
    ) {}



    public record FreteSaveRequest(
        @NotNull  @Min(0) double kg,

        @NotNull @Positive @Min(1) @Max(5000) double km,

        @Min(0) @Max(100) double adicDeUrgencia,

        @NotNull TipoEnvio tipo
        
    ){}

    public record FreteCalculoResponse(
            double valorFrete) {
    }

    public record FreteResponse(
            Long id,
            @Positive @Min(0) double kg,

            @Positive @Min(1) @Max(5000) double km,

            @Positive @Min(0) @Max(100) double adicDeUrgencia,

            TipoEnvio tipo,
            double valorFrete,
            String calculadoEm
            
            ) {
    }
}
