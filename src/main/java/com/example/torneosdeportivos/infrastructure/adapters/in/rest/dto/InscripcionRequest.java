package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscripcionRequest {
    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long idTorneo;

    @NotNull(message = "El número de dorsal es obligatorio")
    private Integer dorsal;
}