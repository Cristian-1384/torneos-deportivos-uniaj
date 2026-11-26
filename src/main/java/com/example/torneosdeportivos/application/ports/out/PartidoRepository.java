package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Wrapper;
import java.util.Optional;

public interface PartidoRepository {
    Partido save(Partido partido);
    Optional<Partido> findById(Long idPartido);

    Wrapper<Partido> getAll(int page, int size, String sortBy, Long idTorneo, Long idEquipo, String estado);

    boolean hasPartidosJugados(Long idEquipo);
}