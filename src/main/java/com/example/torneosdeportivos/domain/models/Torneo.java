package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Torneo implements Serializable {

    private Long idTorneo;
    private String nombre;
    private String disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;     // PLANIFICADO, EN_CURSO, FINALIZADO
    private LocalDateTime fechaCreacion;
}