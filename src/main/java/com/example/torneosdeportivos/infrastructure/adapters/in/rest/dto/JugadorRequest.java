package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JugadorRequest {
    @NotNull(message = "El ID del equipo es obligatorio")
    private Long idEquipo;

    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    private String posicion;
}