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
@Table(name = "partido")
public class PartidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Long idPartido;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "id_equipo_local", nullable = false)
    private Long idEquipoLocal;

    @Column(name = "id_equipo_visitante", nullable = false)
    private Long idEquipoVisitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arbitro")
    private UsuarioEntity arbitro;

    @Column(name = "fecha_partido", nullable = false)
    private LocalDateTime fechaPartido;

    @Column(name = "marcador_local")
    private Integer marcadorLocal;

    @Column(name = "marcador_visitante")
    private Integer marcadorVisitante;

    @Column(nullable = false, length = 20)
    private String estado;
}