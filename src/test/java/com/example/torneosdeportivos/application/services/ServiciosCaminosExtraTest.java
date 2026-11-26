package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.out.*;
import com.example.torneosdeportivos.domain.models.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiciosCaminosExtraTest {

    @Mock private PartidoRepository partidoRepo;
    @Mock private TorneoRepository torneoRepo;
    @Mock private EquipoRepository equipoRepo;
    @Mock private InscripcionRepository inscripcionRepo;
    @Mock private JugadorRepository jugadorRepo;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;
    @Mock private Gson gson;

    @Test
    void partidoService_empateYVictoriaVisitante_yAplazar() {
        PartidoService service = new PartidoService(partidoRepo, torneoRepo, equipoRepo);

        Partido p1 = new Partido();
        p1.setIdPartido(1L);
        p1.setIdEquipoLocal(1L);
        p1.setIdEquipoVisitante(2L);
        p1.setMarcadorLocal(1);
        p1.setMarcadorVisitante(3);

        Partido p2 = new Partido();
        p2.setIdEquipoLocal(1L);
        p2.setIdEquipoVisitante(2L);
        p2.setMarcadorLocal(2);
        p2.setMarcadorVisitante(2);

        Wrapper<Partido> wrapper = new Wrapper<>();
        wrapper.setResults(List.of(p1, p2));

        when(partidoRepo.getAll(0, 1000, "idPartido", 10L, null, "JUGADO")).thenReturn(wrapper);
        when(partidoRepo.getAll(0, 10, "idPartido", 10L, null, null)).thenReturn(wrapper);
        when(equipoRepo.findById(any())).thenReturn(Optional.empty());
        when(partidoRepo.findById(1L)).thenReturn(Optional.of(p1));
        when(partidoRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThat(service.programarPartido(p1).getEstado()).isEqualTo("PROGRAMADO");
        assertThat(service.aplazarPartido(1L).getEstado()).isEqualTo("APLAZADO");
        assertThat(service.listar(0, 10, "idPartido", 10L, null, null)).isNotNull();

        var tabla = service.consultarTablaPosiciones(10L);
        assertThat(tabla).hasSize(2);
        assertThat(tabla.get(0).get("pts")).isEqualTo(4);
    }

    @Test
    void torneoEInscripcion_caminosSecundarios() {
        TorneoService torneoService = new TorneoService(torneoRepo, redisTemplate, gson);
        Wrapper<Torneo> mockWrapper = new Wrapper<>();
        mockWrapper.setTotalElements(5);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn("{\"totalElements\":5}");
        when(gson.fromJson(anyString(), any(Type.class))).thenReturn(mockWrapper);
        when(torneoRepo.findById(1L)).thenReturn(Optional.of(new Torneo()));
        when(torneoRepo.update(any(), any())).thenReturn(new Torneo());

        assertThat(torneoService.listar(0, 10, "idTorneo", null, null, false).getTotalElements()).isEqualTo(5);
        assertThat(torneoService.actualizar(1L, new Torneo())).isNotNull();

        InscripcionService insService = new InscripcionService(inscripcionRepo, jugadorRepo, equipoRepo);
        InscripcionJugador ins = new InscripcionJugador();
        when(inscripcionRepo.getAll(0, 10, "id")).thenReturn(new Wrapper<>());
        when(inscripcionRepo.findById(1L)).thenReturn(Optional.of(ins));
        when(inscripcionRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThat(insService.listar(0, 10, "id")).isNotNull();
        assertThat(insService.retirarInscripcion(1L).getEstado()).isEqualTo("RETIRADO");
    }
}
