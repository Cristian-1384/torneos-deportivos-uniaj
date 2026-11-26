package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipo")
public class EquipoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "cupo_maximo_jugadores", nullable = false)
    private Integer cupoMaximoJugadores;

    @Column(name = "facultad_programa", length = 150)
    private String facultadPrograma;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}