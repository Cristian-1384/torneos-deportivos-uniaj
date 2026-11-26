package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.ports.in.ReportesBatchUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/batch/reportes")
@Tag(name = "Reportes Batch", description = "Ejecución manual bajo demanda de procesos por lotes")
public class ReportesBatchController {

    private final ReportesBatchUseCase reportesBatchUseCase;

    public ReportesBatchController(ReportesBatchUseCase reportesBatchUseCase) {
        this.reportesBatchUseCase = reportesBatchUseCase;
    }

    @PostMapping("/torneos")
    @Operation(summary = "Ejecutar manualmente el Tasklet de reporte de torneos")
    public ResponseEntity<Map<String, String>> lanzarReporteTorneos() {
        reportesBatchUseCase.ejecutarReporteTorneos();
        return ResponseEntity.ok(Map.of("mensaje", "Job ReporteTorneos ejecutado exitosamente. Revisa la consola."));
    }

    @PostMapping("/jugadores-inactivos")
    @Operation(summary = "Ejecutar manualmente el Chunk Job de jugadores inactivos")
    public ResponseEntity<Map<String, String>> lanzarReporteJugadoresInactivos() {
        reportesBatchUseCase.ejecutarReporteJugadoresInactivos();
        return ResponseEntity.ok(Map.of("mensaje", "Job JugadoresInactivos ejecutado exitosamente. Revisa la consola."));
    }
}
