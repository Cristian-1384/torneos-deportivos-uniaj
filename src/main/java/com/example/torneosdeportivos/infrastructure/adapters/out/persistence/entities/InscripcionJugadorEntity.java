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
@Table(name = "inscripcion_jugador")
public class InscripcionJugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @Column(nullable = false)
    private Integer dorsal;

    @Column(name = "fecha_inscripcion", insertable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @Column(nullable = false, length = 20)
    private String estado;
}