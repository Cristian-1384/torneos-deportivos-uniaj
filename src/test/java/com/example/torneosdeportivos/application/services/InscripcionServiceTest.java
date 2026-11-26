package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.out.EquipoRepository;
import com.example.torneosdeportivos.application.ports.out.InscripcionRepository;
import com.example.torneosdeportivos.application.ports.out.JugadorRepository;
import com.example.torneosdeportivos.domain.exceptions.CupoExcedidoException;
import com.example.torneosdeportivos.domain.exceptions.InscripcionDuplicadaException;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Jugador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private JugadorRepository jugadorRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    @Test
    void inscribirJugador_cumpleTodasLasReglas_guardaInscripcion() {
        Jugador jugador = new Jugador();
        jugador.setIdJugador(1L);
        jugador.setIdEquipo(10L);

        Equipo equipo = new Equipo();
        equipo.setIdEquipo(10L);
        equipo.setCupoMaximoJugadores(5);

        InscripcionJugador req = new InscripcionJugador();
        req.setIdJugador(1L);
        req.setIdTorneo(100L);
        req.setDorsal(10);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipo));
        when(inscripcionRepository.existsByIdJugadorAndIdTorneo(1L, 100L)).thenReturn(false);
        when(jugadorRepository.countByEquipoId(10L)).thenReturn(2L);
        when(inscripcionRepository.existsByTorneoAndEquipoAndDorsal(100L, 10L, 10)).thenReturn(false);
        when(inscripcionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        InscripcionJugador resultado = inscripcionService.inscribirJugador(req);

        assertThat(resultado.getEstado()).isEqualTo("INSCRITO");
        verify(inscripcionRepository).save(req);
    }

    @Test
    void inscribirJugador_yaInscrito_lanzaInscripcionDuplicadaException() {
        Jugador jugador = new Jugador();
        jugador.setIdEquipo(10L);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(new Equipo()));
        when(inscripcionRepository.existsByIdJugadorAndIdTorneo(1L, 100L)).thenReturn(true);

        InscripcionJugador req = new InscripcionJugador();
        req.setIdJugador(1L);
        req.setIdTorneo(100L);

        assertThatThrownBy(() -> inscripcionService.inscribirJugador(req))
                .isInstanceOf(InscripcionDuplicadaException.class);
    }

    @Test
    void inscribirJugador_cupoLleno_lanzaCupoExcedidoException() {
        Jugador jugador = new Jugador();
        jugador.setIdEquipo(10L);
        Equipo equipo = new Equipo();
        equipo.setCupoMaximoJugadores(2);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipo));
        when(inscripcionRepository.existsByIdJugadorAndIdTorneo(1L, 100L)).thenReturn(false);
        when(jugadorRepository.countByEquipoId(10L)).thenReturn(2L);

        InscripcionJugador req = new InscripcionJugador();
        req.setIdJugador(1L);
        req.setIdTorneo(100L);

        assertThatThrownBy(() -> inscripcionService.inscribirJugador(req))
                .isInstanceOf(CupoExcedidoException.class);
    }

    @Test
    void inscribirJugador_dorsalRepetido_lanzaReglaDeNegocioException() {
        Jugador jugador = new Jugador();
        jugador.setIdEquipo(10L);
        Equipo equipo = new Equipo();
        equipo.setCupoMaximoJugadores(5);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipo));
        when(inscripcionRepository.existsByIdJugadorAndIdTorneo(1L, 100L)).thenReturn(false);
        when(jugadorRepository.countByEquipoId(10L)).thenReturn(1L);
        when(inscripcionRepository.existsByTorneoAndEquipoAndDorsal(100L, 10L, 10)).thenReturn(true);

        InscripcionJugador req = new InscripcionJugador();
        req.setIdJugador(1L);
        req.setIdTorneo(100L);
        req.setDorsal(10);

        assertThatThrownBy(() -> inscripcionService.inscribirJugador(req))
                .isInstanceOf(ReglaDeNegocioException.class);
    }
}