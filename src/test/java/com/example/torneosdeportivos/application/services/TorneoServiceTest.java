package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.out.TorneoRepository;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private Gson gson;

    @InjectMocks
    private TorneoService torneoService;

    @Test
    void crear_disciplinaValida_guardaTorneoPlanificado() {
        Torneo torneo = new Torneo();
        torneo.setNombre("Copa UNIAJC");
        torneo.setDisciplina("Fútbol");

        when(torneoRepository.save(any(Torneo.class))).thenAnswer(i -> i.getArgument(0));

        Torneo creado = torneoService.crear(torneo);

        assertThat(creado.getEstado()).isEqualTo("PLANIFICADO");
        assertThat(creado.getFechaCreacion()).isNotNull();
        verify(torneoRepository).save(torneo);
    }

    @Test
    void crear_disciplinaNoPermitida_lanzaReglaDeNegocioException() {
        Torneo torneo = new Torneo();
        torneo.setDisciplina("Tenis");

        assertThatThrownBy(() -> torneoService.crear(torneo))
                .isInstanceOf(ReglaDeNegocioException.class);

        verify(torneoRepository, never()).save(any());
    }

    @Test
    void listar_sinCache_consultaRepositorioYGuardaEnRedis() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);

        Wrapper<Torneo> wrapper = new Wrapper<>();
        wrapper.setResults(List.of(new Torneo()));
        when(torneoRepository.getAll(0, 10, "idTorneo", null, null)).thenReturn(wrapper);
        when(gson.toJson(wrapper)).thenReturn("{\"results\":[]}");

        Wrapper<Torneo> resultado = torneoService.listar(0, 10, "idTorneo", null, null, false);

        assertThat(resultado).isEqualTo(wrapper);
        verify(valueOperations).set(anyString(), eq("{\"results\":[]}"), any());
    }

    @Test
    void iniciarTorneo_torneoExiste_cambiaEstadoAEnCurso() {
        Torneo torneo = new Torneo();
        torneo.setIdTorneo(1L);
        torneo.setEstado("PLANIFICADO");

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(torneoRepository.update(eq(1L), any(Torneo.class))).thenAnswer(i -> i.getArgument(1));

        Torneo iniciado = torneoService.iniciarTorneo(1L);

        assertThat(iniciado.getEstado()).isEqualTo("EN_CURSO");
    }

    @Test
    void cerrarTorneo_torneoExiste_cambiaEstadoAFinalizado() {
        Torneo torneo = new Torneo();
        torneo.setIdTorneo(1L);
        torneo.setEstado("EN_CURSO");

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(torneoRepository.update(eq(1L), any(Torneo.class))).thenAnswer(i -> i.getArgument(1));

        Torneo cerrado = torneoService.cerrarTorneo(1L);

        assertThat(cerrado.getEstado()).isEqualTo("FINALIZADO");
    }

    @Test
    void buscarPorId_noExiste_lanzaRecursoNoEncontradoException() {
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> torneoService.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}