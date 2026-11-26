package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.out.EquipoRepository;
import com.example.torneosdeportivos.application.ports.out.PartidoRepository;
import com.example.torneosdeportivos.application.ports.out.TorneoRepository;
import com.example.torneosdeportivos.domain.exceptions.TorneoNoDisponibleException;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartidoServiceTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private PartidoService partidoService;

    @Test
    void registrarResultado_torneoEnCurso_actualizaMarcadorYEstado() {
        Partido partido = new Partido();
        partido.setIdPartido(1L);
        partido.setIdTorneo(10L);

        Torneo torneo = new Torneo();
        torneo.setIdTorneo(10L);
        torneo.setEstado("EN_CURSO");

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(torneoRepository.findById(10L)).thenReturn(Optional.of(torneo));
        when(partidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Partido resultado = partidoService.registrarResultado(1L, 3, 1);

        assertThat(resultado.getEstado()).isEqualTo("JUGADO");
        assertThat(resultado.getMarcadorLocal()).isEqualTo(3);
        assertThat(resultado.getMarcadorVisitante()).isEqualTo(1);
    }

    @Test
    void registrarResultado_torneoPlanificado_lanzaTorneoNoDisponibleException() {
        Partido partido = new Partido();
        partido.setIdTorneo(10L);

        Torneo torneo = new Torneo();
        torneo.setEstado("PLANIFICADO");

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(torneoRepository.findById(10L)).thenReturn(Optional.of(torneo));

        assertThatThrownBy(() -> partidoService.registrarResultado(1L, 2, 0))
                .isInstanceOf(TorneoNoDisponibleException.class);
    }

    @Test
    void consultarTablaPosiciones_calculaPuntosYOrdenaDescendente() {
        Partido p1 = new Partido();
        p1.setIdEquipoLocal(1L);
        p1.setIdEquipoVisitante(2L);
        p1.setMarcadorLocal(2);
        p1.setMarcadorVisitante(0);

        Wrapper<Partido> wrapper = new Wrapper<>();
        wrapper.setResults(List.of(p1));

        Equipo eq1 = new Equipo();
        eq1.setNombre("Sistemas FC");
        Equipo eq2 = new Equipo();
        eq2.setNombre("Admin FC");

        when(partidoRepository.getAll(0, 1000, "idPartido", 10L, null, "JUGADO")).thenReturn(wrapper);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(eq1));
        when(equipoRepository.findById(2L)).thenReturn(Optional.of(eq2));

        List<Map<String, Object>> tabla = partidoService.consultarTablaPosiciones(10L);

        assertThat(tabla).hasSize(2);
        assertThat(tabla.get(0).get("equipo")).isEqualTo("Sistemas FC");
        assertThat(tabla.get(0).get("pts")).isEqualTo(3);
        assertThat(tabla.get(1).get("equipo")).isEqualTo("Admin FC");
        assertThat(tabla.get(1).get("pts")).isEqualTo(0);
    }
}