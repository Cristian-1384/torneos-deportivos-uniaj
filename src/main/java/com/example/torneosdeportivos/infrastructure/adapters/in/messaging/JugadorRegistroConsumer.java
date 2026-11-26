package com.example.torneosdeportivos.infrastructure.adapters.in.messaging;

import com.example.torneosdeportivos.application.ports.in.JugadorUseCase;
import com.example.torneosdeportivos.domain.models.Jugador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class JugadorRegistroConsumer {

    private static final Logger log = LoggerFactory.getLogger(JugadorRegistroConsumer.class);

    private final JugadorUseCase jugadorUseCase;

    public JugadorRegistroConsumer(JugadorUseCase jugadorUseCase) {
        this.jugadorUseCase = jugadorUseCase;
    }

    @RabbitListener(queues = "${app.messaging.rabbitmq.jugadores.queue}")
    public void consumirRegistroJugador(Jugador jugador) {
        log.info("Mensaje recibido de la cola. Guardando jugador con cédula: {}", jugador.getCedula());
        jugadorUseCase.crear(jugador);
    }
}
