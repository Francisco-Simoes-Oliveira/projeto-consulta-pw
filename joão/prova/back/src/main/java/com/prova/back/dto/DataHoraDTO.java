package com.prova.back.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataHoraDTO {

    private LocalDateTime dataHora;
    private String dataHoraFormatada;
}
