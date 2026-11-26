package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.JugadorUseCase;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.JugadorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jugadores")
@Tag(name = "Jugadores", description = "Endpoints para el control individual y fichaje de jugadores")
public class JugadorController {

    private final JugadorUseCase jugadorUseCase;

    public JugadorController(JugadorUseCase jugadorUseCase) {
        this.jugadorUseCase = jugadorUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar un jugador en el sistema")
    public ResponseEntity<JugadorResponse> crear(@Valid @RequestBody JugadorRequest request) {
        Jugador jugador = JugadorMapper.toDomain(request);
        Jugador creado = jugadorUseCase.crear(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(JugadorMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener la ficha técnica de un jugador por su ID")
    public ResponseEntity<JugadorResponse> buscarPorId(@PathVariable Long id) {
        Jugador jugador = jugadorUseCase.buscarPorId(id);
        return ResponseEntity.ok(JugadorMapper.toResponse(jugador));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos informativos de un jugador fijos")
    public ResponseEntity<JugadorResponse> actualizar(@PathVariable Long id, @Valid @RequestBody JugadorRequest request) {
        Jugador jugador = JugadorMapper.toDomain(request);
        Jugador actualizado = jugadorUseCase.actualizar(id, jugador);
        return ResponseEntity.ok(JugadorMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Dar de baja o inactivar un jugador por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jugadorUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
