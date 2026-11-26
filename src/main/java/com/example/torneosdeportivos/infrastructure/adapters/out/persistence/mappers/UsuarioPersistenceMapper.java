package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.RolEntity;

public interface UsuarioPersistenceMapper {

    public static UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) return null;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(domain.getIdUsuario());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setEstado(domain.getEstado());
        entity.setIdJugador(domain.getIdJugador());
        entity.setFechaCreacion(domain.getFechaCreacion());

        if (domain.getRol() != null) {
            RolEntity rolEntity = new RolEntity();
            rolEntity.setIdRol(Long.valueOf(domain.getRol().getIdRol()));
            rolEntity.setNombre(domain.getRol().getNombre());
            rolEntity.setDescripcion(domain.getRol().getDescripcion());
            entity.setRol(rolEntity);
        }

        return entity;
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        Usuario domain = new Usuario();
        domain.setIdUsuario(entity.getIdUsuario());
        domain.setUsername(entity.getUsername());
        domain.setEmail(entity.getEmail());
        domain.setPassword(entity.getPassword());
        domain.setEstado(entity.getEstado());
        domain.setIdJugador(entity.getIdJugador());
        domain.setFechaCreacion(entity.getFechaCreacion());

        if (entity.getRol() != null) {
            Rol rol = new Rol();
            rol.setIdRol(entity.getRol().getIdRol().intValue());
            rol.setNombre(entity.getRol().getNombre());
            rol.setDescripcion(entity.getRol().getDescripcion());
            domain.setRol(rol);
        }

        return domain;
    }
}
