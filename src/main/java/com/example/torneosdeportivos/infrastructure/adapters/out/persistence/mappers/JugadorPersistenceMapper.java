package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;

public interface JugadorPersistenceMapper {

    public static JugadorEntity toEntity(Jugador domain) {
        if (domain == null) return null;
        JugadorEntity entity = new JugadorEntity();
        entity.setIdJugador(domain.getIdJugador());
        entity.setIdEquipo(domain.getIdEquipo());
        entity.setCedula(domain.getCedula());
        entity.setNombres(domain.getNombres());
        entity.setApellidos(domain.getApellidos());
        entity.setPosicion(domain.getPosicion());
        entity.setEstado(domain.getEstado());
        return entity;
    }

    public static Jugador toDomain(JugadorEntity entity) {
        if (entity == null) return null;
        Jugador domain = new Jugador();
        domain.setIdJugador(entity.getIdJugador());
        domain.setIdEquipo(entity.getIdEquipo());
        domain.setCedula(entity.getCedula());
        domain.setNombres(entity.getNombres());
        domain.setApellidos(entity.getApellidos());
        domain.setPosicion(entity.getPosicion());
        domain.setEstado(entity.getEstado());
        return domain;
    }
}