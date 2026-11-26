package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionJugador implements Serializable {

    private Long idInscripcion;
    private Long idJugador;
    private Long idTorneo;
    private Integer dorsal;
    private LocalDateTime fechaInscripcion;
    private String estado; // INSCRITO, SUSPENDIDO, RETIRADO
}