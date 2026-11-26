package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import java.util.Optional;

public interface JugadorRepository {
    Jugador save(Jugador jugador);
    Optional<Jugador> findById(Long idJugador);

    Wrapper<Jugador> getByEquipo(Long idEquipo, int page, int size, String sortBy);

    long countByEquipoId(Long idEquipo);
}