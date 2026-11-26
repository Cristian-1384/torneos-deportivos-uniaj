package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rol {
    private Integer idRol;
    private String nombre;
    private String descripcion;
}