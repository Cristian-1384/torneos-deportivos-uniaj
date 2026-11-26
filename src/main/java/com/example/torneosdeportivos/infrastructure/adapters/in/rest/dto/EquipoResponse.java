package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EquipoResponse {
    private Long idEquipo;
    private String nombre;
    private String facultadPrograma;
    private String estado;
    private LocalDateTime fechaCreacion;
}