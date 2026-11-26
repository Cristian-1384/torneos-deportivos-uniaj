package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.PartidoUseCase;
import com.example.torneosdeportivos.application.ports.out.PartidoRepository;
import com.example.torneosdeportivos.application.ports.out.TorneoRepository;
import com.example.torneosdeportivos.application.ports.out.EquipoRepository;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.exceptions.TorneoNoDisponibleException;
import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PartidoService implements PartidoUseCase {

    private final PartidoRepository partidoRepository;
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;

    public PartidoService(PartidoRepository partidoRepository,
                          TorneoRepository torneoRepository,
                          EquipoRepository equipoRepository) {
        this.partidoRepository = partidoRepository;
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public Partido programarPartido(Partido partido) {
        partido.setEstado("PROGRAMADO");
        return partidoRepository.save(partido);
    }

    @Override
    public Wrapper<Partido> listar(int page, int size, String sortBy, Long idTorneo, Long idEquipo, String estado) {
        return partidoRepository.getAll(page, size, sortBy, idTorneo, idEquipo, estado);
    }

    @Override
    public Partido registrarResultado(Long idPartido, Integer marcadorLocal, Integer marcadorVisitante) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new RecursoNoEncontradoException("Partido no encontrado"));

        Torneo torneo = torneoRepository.findById(partido.getIdTorneo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Torneo no encontrado"));

        if (!"EN_CURSO".equals(torneo.getEstado())) {
            throw new TorneoNoDisponibleException("No se pueden registrar marcadores. El torneo se encuentra en estado: " + torneo.getEstado());
        }

        partido.setMarcadorLocal(marcadorLocal);
        partido.setMarcadorVisitante(marcadorVisitante);
        partido.setEstado("JUGADO");

        return partidoRepository.save(partido);
    }

    @Override
    public Partido aplazarPartido(Long idPartido) {
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new RecursoNoEncontradoException("Partido no encontrado"));
        partido.setEstado("APLAZADO");
        return partidoRepository.save(partido);
    }

    @Override
    public List<Map<String, Object>> consultarTablaPosiciones(Long idTorneo) {
        Wrapper<Partido> partidosWrapper = partidoRepository.getAll(0, 1000, "idPartido", idTorneo, null, "JUGADO");
        List<Partido> partidosJugados = partidosWrapper.getResults();

        Map<Long, Map<String, Object>> tabla = new HashMap<>();

        for (Partido p : partidosJugados) {
            inicializarEquipoEnTabla(tabla, p.getIdEquipoLocal());
            inicializarEquipoEnTabla(tabla, p.getIdEquipoVisitante());

            Map<String, Object> statsLocal = tabla.get(p.getIdEquipoLocal());
            Map<String, Object> statsVisitante = tabla.get(p.getIdEquipoVisitante());

            statsLocal.put("pj", (int) statsLocal.get("pj") + 1);
            statsVisitante.put("pj", (int) statsVisitante.get("pj") + 1);

            if (p.getMarcadorLocal() > p.getMarcadorVisitante()) {
                statsLocal.put("pts", (int) statsLocal.get("pts") + 3);
                statsLocal.put("pg", (int) statsLocal.get("pg") + 1);
                statsVisitante.put("pe", (int) statsVisitante.get("pe") + 1);
            } else if (p.getMarcadorLocal() < p.getMarcadorVisitante()) {
                statsVisitante.put("pts", (int) statsVisitante.get("pts") + 3);
                statsVisitante.put("pg", (int) statsVisitante.get("pg") + 1);
                statsLocal.put("pe", (int) statsLocal.get("pe") + 1);
            } else {
                statsLocal.put("pts", (int) statsLocal.get("pts") + 1);
                statsVisitante.put("pts", (int) statsVisitante.get("pts") + 1);
                statsLocal.put("pe", (int) statsLocal.get("pe") + 1);
                statsVisitante.put("pe", (int) statsVisitante.get("pe") + 1);
            }
        }

        List<Map<String, Object>> listaOrdenada = new ArrayList<>(tabla.values());
        listaOrdenada.sort((a, b) -> Integer.compare((int) b.get("pts"), (int) a.get("pts"))); // Ordenar por puntos descendente

        return listaOrdenada;
    }

    private void inicializarEquipoEnTabla(Map<Long, Map<String, Object>> tabla, Long idEquipo) {
        if (!tabla.containsKey(idEquipo)) {
            Map<String, Object> stats = new HashMap<>();
            Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
            stats.put("equipo", equipo != null ? equipo.getNombre() : "Equipo " + idEquipo);
            stats.put("pj", 0);
            stats.put("pg", 0);
            stats.put("pe", 0);
            stats.put("pts", 0);
            tabla.put(idEquipo, stats);
        }
    }
}