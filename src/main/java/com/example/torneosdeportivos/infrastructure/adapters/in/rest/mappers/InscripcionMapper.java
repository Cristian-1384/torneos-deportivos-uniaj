package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.InscripcionRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.InscripcionResponse;

public interface InscripcionMapper {

    public static InscripcionJugador toDomain(InscripcionRequest request) {
        if (request == null) return null;

        InscripcionJugador inscripcion = new InscripcionJugador();
        inscripcion.setIdJugador(request.getIdJugador());
        inscripcion.setIdTorneo(request.getIdTorneo());
        inscripcion.setDorsal(request.getDorsal());
        return inscripcion;
    }

    public static InscripcionResponse toResponse(InscripcionJugador domain) {
        if (domain == null) return null;

        InscripcionResponse response = new InscripcionResponse();
        response.setIdInscripcion(domain.getIdInscripcion());
        response.setIdJugador(domain.getIdJugador());
        response.setIdTorneo(domain.getIdTorneo());
        response.setDorsal(domain.getDorsal());
        response.setFechaInscripcion(domain.getFechaInscripcion());
        response.setEstado(domain.getEstado());
        return response;
    }
}