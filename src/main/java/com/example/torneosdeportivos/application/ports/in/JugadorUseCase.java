package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Wrapper;

public interface JugadorUseCase {

    Jugador crear(Jugador jugador);

    Wrapper<Jugador> listar(int page, int size, String sortBy);

    Jugador buscarPorId(Long idJugador);

    Jugador actualizar(Long idJugador, Jugador jugador);

    void eliminar(Long idJugador);
}