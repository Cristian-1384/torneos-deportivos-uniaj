package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipoRequest {
    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @NotBlank(message = "La facultad o programa es obligatoria")
    private String facultadPrograma;

    @NotNull(message = "El cupo máximo de jugadores es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser mayor a 0")
    private Integer cupoMaximoJugadores;
}