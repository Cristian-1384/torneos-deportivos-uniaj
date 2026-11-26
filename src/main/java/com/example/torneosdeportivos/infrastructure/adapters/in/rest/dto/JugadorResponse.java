package com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto;

import lombok.Data;

@Data
public class JugadorResponse {
    private Long idJugador;
    private Long idEquipo;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String posicion;
    private String estado;
}