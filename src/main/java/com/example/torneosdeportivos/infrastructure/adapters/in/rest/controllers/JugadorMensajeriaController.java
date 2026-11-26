package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.JugadorMensajeriaUseCase;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.JugadorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mensajeria/jugadores")
@Tag(name = "Mensajería Jugadores", description = "Registro asíncrono de jugadores mediante RabbitMQ")
public class JugadorMensajeriaController {

    private final JugadorMensajeriaUseCase jugadorMensajeriaUseCase;

    public JugadorMensajeriaController(JugadorMensajeriaUseCase jugadorMensajeriaUseCase) {
        this.jugadorMensajeriaUseCase = jugadorMensajeriaUseCase;
    }

    @PostMapping("/registro")
    @Operation(summary = "Enviar solicitud de registro de jugador a la cola de RabbitMQ")
    public ResponseEntity<Map<String, String>> registrarPorCola(@Valid @RequestBody JugadorRequest request) {
        Jugador jugador = JugadorMapper.toDomain(request);
        jugadorMensajeriaUseCase.enviarRegistroACola(jugador);

        return ResponseEntity.accepted().body(Map.of(
                "mensaje", "Solicitud de registro de jugador enviada a la cola exitosamente",
                "cedula", request.getCedula()
        ));
    }
}
