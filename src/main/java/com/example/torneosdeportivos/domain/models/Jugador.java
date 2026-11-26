package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jugador implements Serializable {

    private Long idJugador;
    private Long idEquipo;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String posicion;
    private String estado; // ACTIVO, INACTIVO
}