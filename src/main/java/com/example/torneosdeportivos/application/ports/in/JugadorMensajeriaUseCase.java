package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.Jugador;

public interface JugadorMensajeriaUseCase {
    void enviarRegistroACola(Jugador jugador);
}