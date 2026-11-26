package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.EquipoUseCase;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.EquipoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.EquipoResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.EquipoMapper;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.JugadorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipos")
@Tag(name = "Equipos", description = "Endpoints para la gestión de equipos universitarios y sus nóminas")
public class EquipoController {

    private final EquipoUseCase equipoUseCase;

    public EquipoController(EquipoUseCase equipoUseCase) {
        this.equipoUseCase = equipoUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo equipo en el sistema")
    public ResponseEntity<EquipoResponse> crear(@Valid @RequestBody EquipoRequest request) {
        Equipo equipo = EquipoMapper.toDomain(request);
        Equipo creado = equipoUseCase.crear(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(EquipoMapper.toResponse(creado));
    }

    @GetMapping
    @Operation(summary = "Listar equipos de forma paginada y filtrada por nombre o estado")
    public ResponseEntity<Wrapper<EquipoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idEquipo") String sortBy,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String nombre) {

        Wrapper<Equipo> equipos = equipoUseCase.listar(page, size, sortBy, estado, nombre);

        Wrapper<EquipoResponse> responseWrapper = new Wrapper<>();
        responseWrapper.setTotalElements(equipos.getTotalElements());
        responseWrapper.setTotalPages(equipos.getTotalPages());
        responseWrapper.setNumberOfElements(equipos.getNumberOfElements());
        responseWrapper.setSize(equipos.getSize());
        responseWrapper.setResults(equipos.getResults().stream().map(EquipoMapper::toResponse).toList());

        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar los detalles de un equipo por su ID")
    public ResponseEntity<EquipoResponse> buscarPorId(@PathVariable Long id) {
        Equipo equipo = equipoUseCase.buscarPorId(id);
        return ResponseEntity.ok(EquipoMapper.toResponse(equipo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar la información básica de un equipo")
    public ResponseEntity<EquipoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
        Equipo equipo = EquipoMapper.toDomain(request);
        Equipo actualizado = equipoUseCase.actualizar(id, equipo);
        return ResponseEntity.ok(EquipoMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar o inactivar lógicamente un equipo (Aplica Regla de Negocio #4)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        equipoUseCase.eliminarOInactivar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/jugadores")
    @Operation(summary = "Consultar de manera paginada la lista de integrantes vinculados a un equipo")
    public ResponseEntity<Wrapper<JugadorResponse>> consultarJugadores(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idJugador") String sortBy) {

        Wrapper<Jugador> jugadores = equipoUseCase.consultarJugadores(id, page, size, sortBy);

        Wrapper<JugadorResponse> responseWrapper = new Wrapper<>();
        responseWrapper.setTotalElements(jugadores.getTotalElements());
        responseWrapper.setTotalPages(jugadores.getTotalPages());
        responseWrapper.setNumberOfElements(jugadores.getNumberOfElements());
        responseWrapper.setSize(jugadores.getSize());
        responseWrapper.setResults(jugadores.getResults().stream().map(JugadorMapper::toResponse).toList());

        return ResponseEntity.ok(responseWrapper);
    }
}