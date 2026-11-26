package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.domain.models.*;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.*;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistenceAdaptersTest {

    @Mock private TorneoJpaRepository torneoJpa;
    @Mock private EquipoJpaRepository equipoJpa;
    @Mock private JugadorJpaRepository jugadorJpa;
    @Mock private InscripcionJugadorJpaRepository inscripcionJpa;
    @Mock private PartidoJpaRepository partidoJpa;

    @Test
    void todosLosAdaptadores_cubrenOperaciones() {
        // 1. PostgresqlAdapter (Torneos)
        PostgresqlAdapter torneoAdapter = new PostgresqlAdapter(torneoJpa);
        when(torneoJpa.save(any())).thenReturn(new TorneoEntity());
        when(torneoJpa.findById(1L)).thenReturn(Optional.of(new TorneoEntity()));
        when(torneoJpa.findByFiltros(any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new TorneoEntity())));

        assertThat(torneoAdapter.save(new Torneo())).isNotNull();
        assertThat(torneoAdapter.findById(1L)).isPresent();
        assertThat(torneoAdapter.update(1L, new Torneo())).isNotNull();
        assertThat(torneoAdapter.getAll(0, 10, "idTorneo", null, null).getTotalElements()).isEqualTo(1);

        // 2. EquipoPersistenceAdapter
        EquipoPersistenceAdapter equipoAdapter = new EquipoPersistenceAdapter(equipoJpa);
        when(equipoJpa.save(any())).thenReturn(new EquipoEntity());
        when(equipoJpa.findById(1L)).thenReturn(Optional.of(new EquipoEntity()));
        when(equipoJpa.existsById(1L)).thenReturn(true);
        when(equipoJpa.findByFiltros(any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new EquipoEntity())));

        assertThat(equipoAdapter.save(new Equipo())).isNotNull();
        assertThat(equipoAdapter.findById(1L)).isPresent();
        assertThat(equipoAdapter.existsById(1L)).isTrue();
        assertThat(equipoAdapter.getAll(0, 10, "idEquipo", null, null).getTotalElements()).isEqualTo(1);

        // 3. JugadorPersistenceAdapter
        JugadorPersistenceAdapter jugadorAdapter = new JugadorPersistenceAdapter(jugadorJpa);
        when(jugadorJpa.save(any())).thenReturn(new JugadorEntity());
        when(jugadorJpa.findById(1L)).thenReturn(Optional.of(new JugadorEntity()));
        when(jugadorJpa.countJugadoresActivos(1L)).thenReturn(5L);
        when(jugadorJpa.findByIdEquipo(any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new JugadorEntity())));

        assertThat(jugadorAdapter.save(new Jugador())).isNotNull();
        assertThat(jugadorAdapter.findById(1L)).isPresent();
        assertThat(jugadorAdapter.countByEquipoId(1L)).isEqualTo(5L);
        assertThat(jugadorAdapter.getByEquipo(1L, 0, 10, "idJugador").getTotalElements()).isEqualTo(1);

        // 4. InscripcionPersistenceAdapter
        InscripcionPersistenceAdapter insAdapter = new InscripcionPersistenceAdapter(inscripcionJpa);
        when(inscripcionJpa.save(any())).thenReturn(new InscripcionJugadorEntity());
        when(inscripcionJpa.findById(1L)).thenReturn(Optional.of(new InscripcionJugadorEntity()));
        when(inscripcionJpa.existsByIdJugadorAndIdTorneo(1L, 1L)).thenReturn(true);
        when(inscripcionJpa.existsDorsalDuplicadoEnEquipoYTorneo(1L, 1L, 10)).thenReturn(false);
        when(inscripcionJpa.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new InscripcionJugadorEntity())));

        assertThat(insAdapter.save(new InscripcionJugador())).isNotNull();
        assertThat(insAdapter.findById(1L)).isPresent();
        assertThat(insAdapter.existsByIdJugadorAndIdTorneo(1L, 1L)).isTrue();
        assertThat(insAdapter.existsByTorneoAndEquipoAndDorsal(1L, 1L, 10)).isFalse();
        assertThat(insAdapter.getAll(0, 10, "idInscripcion").getTotalElements()).isEqualTo(1);

        // 5. PartidoPersistenceAdapter
        PartidoPersistenceAdapter partidoAdapter = new PartidoPersistenceAdapter(partidoJpa);
        when(partidoJpa.save(any())).thenReturn(new PartidoEntity());
        when(partidoJpa.findById(1L)).thenReturn(Optional.of(new PartidoEntity()));
        when(partidoJpa.hasPartidosJugados(1L)).thenReturn(true);
        when(partidoJpa.findByFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new PartidoEntity())));

        assertThat(partidoAdapter.save(new Partido())).isNotNull();
        assertThat(partidoAdapter.findById(1L)).isPresent();
        assertThat(partidoAdapter.hasPartidosJugados(1L)).isTrue();
        assertThat(partidoAdapter.getAll(0, 10, "idPartido", null, null, null).getTotalElements()).isEqualTo(1);
    }
}
