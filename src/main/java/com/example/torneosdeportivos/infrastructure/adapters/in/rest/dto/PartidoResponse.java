package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartidoResponse {
    private Long idPartido;
    private Long idTorneo;
    private Long idEquipoLocal;
    private Long idEquipoVisitante;
    private Long idArbitro;
    private LocalDateTime fechaPartido;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private String estado;
}