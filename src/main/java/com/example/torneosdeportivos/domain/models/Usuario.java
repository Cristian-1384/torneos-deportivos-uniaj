package com.example.torneosdeportivos.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long idUsuario;
    private String username;
    private String email;
    private String password;
    private String estado;   // ACTIVO, BLOQUEADO
    private Rol rol;
    private Long idJugador;
    private LocalDateTime fechaCreacion;
}