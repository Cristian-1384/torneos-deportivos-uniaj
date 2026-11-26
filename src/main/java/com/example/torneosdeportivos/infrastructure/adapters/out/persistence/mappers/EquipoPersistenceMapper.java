package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.EquipoEntity;

public interface EquipoPersistenceMapper {

    public static EquipoEntity toEntity(Equipo domain) {
        if (domain == null) return null;
        EquipoEntity entity = new EquipoEntity();
        entity.setIdEquipo(domain.getIdEquipo());
        entity.setNombre(domain.getNombre());
        entity.setFacultadPrograma(domain.getFacultadPrograma());
        entity.setCupoMaximoJugadores(domain.getCupoMaximoJugadores());
        entity.setEstado(domain.getEstado());
        entity.setFechaCreacion(domain.getFechaCreacion());
        return entity;
    }

    public static Equipo toDomain(EquipoEntity entity) {
        if (entity == null) return null;
        Equipo domain = new Equipo();
        domain.setIdEquipo(entity.getIdEquipo());
        domain.setNombre(entity.getNombre());
        domain.setFacultadPrograma(entity.getFacultadPrograma());
        domain.setCupoMaximoJugadores(entity.getCupoMaximoJugadores());
        domain.setEstado(entity.getEstado());
        domain.setFechaCreacion(entity.getFechaCreacion());
        return domain;
    }
}