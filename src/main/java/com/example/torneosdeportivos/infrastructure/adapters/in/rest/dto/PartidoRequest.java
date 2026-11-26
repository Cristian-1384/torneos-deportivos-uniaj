package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartidoRequest {
    @NotNull(message = "El ID del torneo es obligatorio")
    private Long idTorneo;

    @NotNull(message = "El ID del equipo local es obligatorio")
    private Long idEquipoLocal;

    @NotNull(message = "El ID del equipo visitante es obligatorio")
    private Long idEquipoVisitante;

    private Long idArbitro; // Opcional / Nullable al programar

    @NotNull(message = "La fecha y hora del partido son obligatorias")
    private LocalDateTime fechaPartido;
}