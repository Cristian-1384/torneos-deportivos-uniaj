package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.PartidoUseCase;
import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.PartidoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.PartidoResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.ResultadoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.PartidoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidos")
@Tag(name = "Partidos", description = "Endpoints para la programación de encuentros, registro de resultados y tablas matemáticas")
public class PartidoController {

    private final PartidoUseCase partidoUseCase;

    public PartidoController(PartidoUseCase partidoUseCase) {
        this.partidoUseCase = partidoUseCase;
    }

    @PostMapping
    @Operation(summary = "Programar un nuevo partido oficial en el sistema")
    public ResponseEntity<PartidoResponse> programar(@Valid @RequestBody PartidoRequest request) {
        Partido partido = PartidoMapper.toDomain(request);
        Partido programado = partidoUseCase.programarPartido(partido);
        return ResponseEntity.status(HttpStatus.CREATED).body(PartidoMapper.toResponse(programado));
    }

    @GetMapping
    @Operation(summary = "Listar partidos de forma paginada aplicando filtros opcionales de torneo, equipo o estado")
    public ResponseEntity<Wrapper<PartidoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idPartido") String sortBy,
            @RequestParam(required = false) Long idTorneo,
            @RequestParam(required = false) Long idEquipo,
            @RequestParam(required = false) String estado) {

        Wrapper<Partido> partidos = partidoUseCase.listar(page, size, sortBy, idTorneo, idEquipo, estado);

        Wrapper<PartidoResponse> responseWrapper = new Wrapper<>();
        responseWrapper.setTotalElements(partidos.getTotalElements());
        responseWrapper.setTotalPages(partidos.getTotalPages());
        responseWrapper.setNumberOfElements(partidos.getNumberOfElements());
        responseWrapper.setSize(partidos.getSize());
        responseWrapper.setResults(partidos.getResults().stream().map(PartidoMapper::toResponse).toList());

        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping("/{id}/resultado")
    @Operation(summary = "Registrar el marcador final de un partido (Aplica Regla de Negocio #3)")
    public ResponseEntity<PartidoResponse> registrarResultado(
            @PathVariable Long id,
            @Valid @RequestBody ResultadoRequest request) {

        Partido partidoModificado = partidoUseCase.registrarResultado(id, request.getMarcadorLocal(), request.getMarcadorVisitante());
        return ResponseEntity.ok(PartidoMapper.toResponse(partidoModificado));
    }

    @PutMapping("/{id}/aplazar")
    @Operation(summary = "Cambiar el estado de un encuentro deportivo programado a APLAZADO")
    public ResponseEntity<PartidoResponse> aplazar(@PathVariable Long id) {
        Partido partidoAplazado = partidoUseCase.aplazarPartido(id);
        return ResponseEntity.ok(PartidoMapper.toResponse(partidoAplazado));
    }

    @GetMapping("/torneo/{idTorneo}/tabla-posiciones")
    @Operation(summary = "Calcular de forma matemática y dinámica la tabla de posiciones (Regla de Negocio #5)")
    public ResponseEntity<List<Map<String, Object>>> consultarTablaPosiciones(@PathVariable Long idTorneo) {
        List<Map<String, Object>> tabla = partidoUseCase.consultarTablaPosiciones(idTorneo);
        return ResponseEntity.ok(tabla);
    }
}