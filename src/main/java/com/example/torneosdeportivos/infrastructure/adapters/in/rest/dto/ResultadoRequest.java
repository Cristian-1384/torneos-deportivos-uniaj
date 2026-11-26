package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultadoRequest {
    @NotNull(message = "El marcador local es obligatorio")
    @Min(value = 0, message = "El marcador no puede ser negativo")
    private Integer marcadorLocal;

    @NotNull(message = "El marcador visitante es obligatorio")
    @Min(value = 0, message = "El marcador no puede ser negativo")
    private Integer marcadorVisitante;
}