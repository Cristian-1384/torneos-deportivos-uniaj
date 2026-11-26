package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Jugador;

public interface JugadorMessagePublisher {
    void publicarRegistroJugador(Jugador jugador);
}