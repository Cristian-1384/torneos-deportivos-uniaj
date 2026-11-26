package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.TorneoUseCase;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.CreateTorneoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.TorneoResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.TorneoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@Tag(name = "Torneos", description = "Gestión del ciclo de vida y consultas de torneos deportivos")
public class TorneoController {

    private final TorneoUseCase torneoUseCase;

    public TorneoController(TorneoUseCase torneoUseCase) {
        this.torneoUseCase = torneoUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo torneo (Acceso: ADMIN)")
    public ResponseEntity<TorneoResponse> crear(@Valid @RequestBody CreateTorneoRequest request) {
        Torneo torneo = TorneoMapper.INSTANCE.toTorneo(request);
        Torneo creado = torneoUseCase.crear(torneo);

        return ResponseEntity.status(HttpStatus.CREATED).body(TorneoMapper.INSTANCE.toTorneoResponse(creado));
    }

    @GetMapping
    @Operation(summary = "Listar torneos de forma paginada y filtrada con control de caché")
    public ResponseEntity<Wrapper<Torneo>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idTorneo") String sortBy,
            @RequestParam(required = false) String disciplina,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "false") boolean refresh) {

        Wrapper<Torneo> torneos = torneoUseCase.listar(page, size, sortBy, disciplina, estado, refresh);
        return ResponseEntity.ok(torneos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de un torneo por su ID")
    public ResponseEntity<TorneoResponse> buscarPorId(@PathVariable Long id) {
        Torneo torneo = torneoUseCase.buscarPorId(id);
        return ResponseEntity.ok(TorneoMapper.INSTANCE.toTorneoResponse(torneo));
    }

    @PutMapping("/{id}/iniciar")
    @Operation(summary = "Cambiar el estado de un torneo a EN_CURSO (Acceso: ADMIN)")
    public ResponseEntity<TorneoResponse> iniciarTorneo(@PathVariable Long id) {
        Torneo torneo = torneoUseCase.iniciarTorneo(id);
        return ResponseEntity.ok(TorneoMapper.INSTANCE.toTorneoResponse(torneo));
    }

    @PutMapping("/{id}/cerrar")
    @Operation(summary = "Cambiar el estado de un torneo a FINALIZADO (Acceso: ADMIN)")
    public ResponseEntity<TorneoResponse> cerrarTorneo(@PathVariable Long id) {
        Torneo torneo = torneoUseCase.cerrarTorneo(id);
        return ResponseEntity.ok(TorneoMapper.INSTANCE.toTorneoResponse(torneo));
    }
}
