package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.PartidoEntity;

public interface PartidoPersistenceMapper {

    public static PartidoEntity toEntity(Partido domain) {
        if (domain == null) return null;
        PartidoEntity entity = new PartidoEntity();
        entity.setIdPartido(domain.getIdPartido());
        entity.setIdTorneo(domain.getIdTorneo());
        entity.setIdEquipoLocal(domain.getIdEquipoLocal());
        entity.setIdEquipoVisitante(domain.getIdEquipoVisitante());
        entity.setFechaPartido(domain.getFechaPartido());
        entity.setMarcadorLocal(domain.getMarcadorLocal());
        entity.setMarcadorVisitante(domain.getMarcadorVisitante());
        entity.setEstado(domain.getEstado());

        if (domain.getArbitro() != null) {
            entity.setArbitro(UsuarioPersistenceMapper.toEntity(domain.getArbitro()));
        }
        return entity;
    }

    public static Partido toDomain(PartidoEntity entity) {
        if (entity == null) return null;
        Partido domain = new Partido();
        domain.setIdPartido(entity.getIdPartido());
        domain.setIdTorneo(entity.getIdTorneo());
        domain.setIdEquipoLocal(entity.getIdEquipoLocal());
        domain.setIdEquipoVisitante(entity.getIdEquipoVisitante());
        domain.setFechaPartido(entity.getFechaPartido());
        domain.setMarcadorLocal(entity.getMarcadorLocal());
        domain.setMarcadorVisitante(entity.getMarcadorVisitante());
        domain.setEstado(entity.getEstado());

        if (entity.getArbitro() != null) {
            domain.setArbitro(UsuarioPersistenceMapper.toDomain(entity.getArbitro()));
        }
        return domain;
    }
}