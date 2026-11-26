package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscripcionResponse {
    private Long idInscripcion;
    private Long idJugador;
    private Long idTorneo;
    private Integer dorsal;
    private LocalDateTime fechaInscripcion;
    private String estado;
}