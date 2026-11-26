package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.InscripcionUseCase;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.InscripcionRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.InscripcionResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.InscripcionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "Endpoints para la inscripción y retiro de jugadores en torneos activos")
public class InscripcionController {

    private final InscripcionUseCase inscripcionUseCase;

    public InscripcionController(InscripcionUseCase inscripcionUseCase) {
        this.inscripcionUseCase = inscripcionUseCase;
    }

    @PostMapping
    @Operation(summary = "Inscribir un jugador a un torneo (Aplica Reglas de Negocio #1, #2 y #6)")
    public ResponseEntity<InscripcionResponse> inscribir(@Valid @RequestBody InscripcionRequest request) {
        InscripcionJugador inscripcion = InscripcionMapper.toDomain(request);
        InscripcionJugador creada = inscripcionUseCase.inscribirJugador(inscripcion);
        return ResponseEntity.status(HttpStatus.CREATED).body(InscripcionMapper.toResponse(creada));
    }

    @GetMapping
    @Operation(summary = "Listar de manera paginada el historial completo de inscripciones")
    public ResponseEntity<Wrapper<InscripcionResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idInscripcion") String sortBy) {

        Wrapper<InscripcionJugador> inscripciones = inscripcionUseCase.listar(page, size, sortBy);

        Wrapper<InscripcionResponse> responseWrapper = new Wrapper<>();
        responseWrapper.setTotalElements(inscripciones.getTotalElements());
        responseWrapper.setTotalPages(inscripciones.getTotalPages());
        responseWrapper.setNumberOfElements(inscripciones.getNumberOfElements());
        responseWrapper.setSize(inscripciones.getSize());
        responseWrapper.setResults(inscripciones.getResults().stream().map(InscripcionMapper::toResponse).toList());

        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping("/{id}/retirar")
    @Operation(summary = "Retirar de forma definitiva la inscripción de un estudiante")
    public ResponseEntity<InscripcionResponse> retirar(@PathVariable Long id) {
        InscripcionJugador retirada = inscripcionUseCase.retirarInscripcion(id);
        return ResponseEntity.ok(InscripcionMapper.toResponse(retirada));
    }
}