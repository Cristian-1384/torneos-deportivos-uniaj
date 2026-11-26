package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.JugadorResponse;

public interface JugadorMapper {

    public static Jugador toDomain(JugadorRequest request) {
        if (request == null) return null;

        Jugador jugador = new Jugador();
        jugador.setIdEquipo(request.getIdEquipo());
        jugador.setCedula(request.getCedula());
        jugador.setNombres(request.getNombres());
        jugador.setApellidos(request.getApellidos());
        jugador.setPosicion(request.getPosicion());
        return jugador;
    }

    public static JugadorResponse toResponse(Jugador domain) {
        if (domain == null) return null;

        JugadorResponse response = new JugadorResponse();
        response.setIdJugador(domain.getIdJugador());
        response.setIdEquipo(domain.getIdEquipo());
        response.setCedula(domain.getCedula());
        response.setNombres(domain.getNombres());
        response.setApellidos(domain.getApellidos());
        response.setPosicion(domain.getPosicion());
        response.setEstado(domain.getEstado());
        return response;
    }
}