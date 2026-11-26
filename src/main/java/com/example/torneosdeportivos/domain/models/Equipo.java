package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipo implements Serializable {

    private Long idEquipo;
    private String nombre;
    private String facultadPrograma;
    private Integer cupoMaximoJugadores;
    private String estado; // ACTIVO, INACTIVO
    private LocalDateTime fechaCreacion;
}
