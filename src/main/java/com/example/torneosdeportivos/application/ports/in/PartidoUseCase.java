package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Wrapper;
import java.util.List;
import java.util.Map;

public interface PartidoUseCase {

    Partido programarPartido(Partido partido);

    Wrapper<Partido> listar(int page, int size, String sortBy, Long idTorneo, Long idEquipo, String estado);

    Partido registrarResultado(Long idPartido, Integer marcadorLocal, Integer marcadorVisitante);

    Partido aplazarPartido(Long idPartido);

    List<Map<String, Object>> consultarTablaPosiciones(Long idTorneo);
}