package br.com.provabase.item.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
        String name,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String description,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "Preço não pode ser negativo")
        BigDecimal price,

        Boolean active
) {}
