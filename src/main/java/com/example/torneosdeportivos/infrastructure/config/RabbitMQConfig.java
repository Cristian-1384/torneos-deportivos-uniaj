package com.example.torneosdeportivos.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.messaging.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.messaging.rabbitmq.jugadores.queue}")
    private String jugadoresQueueName;

    @Value("${app.messaging.rabbitmq.jugadores.routing-key}")
    private String jugadoresRoutingKey;

    @Bean
    public DirectExchange torneosDeportivosExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue jugadoresRegistroQueue() {
        return new Queue(jugadoresQueueName, true);
    }

    @Bean
    public Binding jugadoresRegistroBinding(Queue jugadoresRegistroQueue,
                                            DirectExchange torneosDeportivosExchange) {
        return BindingBuilder.bind(jugadoresRegistroQueue)
                .to(torneosDeportivosExchange)
                .with(jugadoresRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}