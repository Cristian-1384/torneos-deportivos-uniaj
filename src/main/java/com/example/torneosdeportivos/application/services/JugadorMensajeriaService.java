package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.JugadorMensajeriaUseCase;
import com.example.torneosdeportivos.application.ports.out.JugadorMessagePublisher;
import com.example.torneosdeportivos.domain.models.Jugador;
import org.springframework.stereotype.Service;

@Service
public class JugadorMensajeriaService implements JugadorMensajeriaUseCase {

    private final JugadorMessagePublisher jugadorMessagePublisher;

    public JugadorMensajeriaService(JugadorMessagePublisher jugadorMessagePublisher) {
        this.jugadorMessagePublisher = jugadorMessagePublisher;
    }

    @Override
    public void enviarRegistroACola(Jugador jugador) {
        jugadorMessagePublisher.publicarRegistroJugador(jugador);
    }
}