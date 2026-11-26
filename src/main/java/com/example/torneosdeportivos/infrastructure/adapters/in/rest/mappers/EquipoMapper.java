package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.EquipoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.EquipoResponse;

public interface EquipoMapper {

    public static Equipo toDomain(EquipoRequest request) {
        if (request == null) return null;

        Equipo equipo = new Equipo();
        equipo.setNombre(request.getNombre());
        equipo.setFacultadPrograma(request.getFacultadPrograma());
        equipo.setCupoMaximoJugadores(request.getCupoMaximoJugadores());
        return equipo;
    }

    public static EquipoResponse toResponse(Equipo domain) {
        if (domain == null) return null;

        EquipoResponse response = new EquipoResponse();
        response.setIdEquipo(domain.getIdEquipo());
        response.setNombre(domain.getNombre());
        response.setFacultadPrograma(domain.getFacultadPrograma());
        response.setEstado(domain.getEstado());
        response.setFechaCreacion(domain.getFechaCreacion());
        return response;
    }
}