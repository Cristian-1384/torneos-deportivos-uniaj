package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import java.util.Optional;

public interface InscripcionRepository {
    InscripcionJugador save(InscripcionJugador inscripcion);
    Optional<InscripcionJugador> findById(Long idInscripcion);

    boolean existsByIdJugadorAndIdTorneo(Long idJugador, Long idTorneo);

    boolean existsByTorneoAndEquipoAndDorsal(Long idTorneo, Long idEquipo, Integer dorsal);

    Wrapper<InscripcionJugador> getAll(int page, int size, String sortBy);
}