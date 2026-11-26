package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partido {
    private Long idPartido;
    private Long idTorneo;
    private Long idEquipoLocal;
    private Long idEquipoVisitante;
    private Usuario arbitro; // <-- Verifica que se llame exactamente 'arbitro' en minúscula
    private LocalDateTime fechaPartido;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private String estado;
}