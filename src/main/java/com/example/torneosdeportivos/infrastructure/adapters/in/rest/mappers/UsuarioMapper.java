package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.enums.Roles;
import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.RegistroRequest;

import java.time.LocalDateTime;

public interface UsuarioMapper {

    public static Usuario toDomain(RegistroRequest request) {
        if (request == null) return null;

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDateTime.now());

        if (request.getIdRol() != null) {
            Roles enumRol = Roles.buscarPorId(request.getIdRol());

            Rol rolDominio = new Rol();
            rolDominio.setIdRol(enumRol.getId());
            rolDominio.setNombre(enumRol.name());

            usuario.setRol(rolDominio);
        }

        return usuario;
    }
}
