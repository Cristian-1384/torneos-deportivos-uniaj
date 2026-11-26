package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.InscripcionJugadorEntity;

public interface InscripcionPersistenceMapper {

    public static InscripcionJugadorEntity toEntity(InscripcionJugador domain) {
        if (domain == null) return null;
        InscripcionJugadorEntity entity = new InscripcionJugadorEntity();
        entity.setIdInscripcion(domain.getIdInscripcion());
        entity.setIdJugador(domain.getIdJugador());
        entity.setIdTorneo(domain.getIdTorneo());
        entity.setDorsal(domain.getDorsal());
        entity.setFechaInscripcion(domain.getFechaInscripcion());
        entity.setEstado(domain.getEstado());
        return entity;
    }

    public static InscripcionJugador toDomain(InscripcionJugadorEntity entity) {
        if (entity == null) return null;
        InscripcionJugador domain = new InscripcionJugador();
        domain.setIdInscripcion(entity.getIdInscripcion());
        domain.setIdJugador(entity.getIdJugador());
        domain.setIdTorneo(entity.getIdTorneo());
        domain.setDorsal(entity.getDorsal());
        domain.setFechaInscripcion(entity.getFechaInscripcion());
        domain.setEstado(entity.getEstado());
        return domain;
    }
}