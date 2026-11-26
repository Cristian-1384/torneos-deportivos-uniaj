package com.example.torneosdeportivos.infrastructure.adapters.out.messaging;

import com.example.torneosdeportivos.application.ports.out.JugadorMessagePublisher;
import com.example.torneosdeportivos.domain.models.Jugador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitJugadorMessagePublisherAdapter implements JugadorMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitJugadorMessagePublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.messaging.rabbitmq.jugadores.routing-key}")
    private String routingKey;

    public RabbitJugadorMessagePublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarRegistroJugador(Jugador jugador) {
        log.info("Publicando en la cola el registro del jugador con cédula: {}", jugador.getCedula());
        rabbitTemplate.convertAndSend(exchange, routingKey, jugador);
    }
}