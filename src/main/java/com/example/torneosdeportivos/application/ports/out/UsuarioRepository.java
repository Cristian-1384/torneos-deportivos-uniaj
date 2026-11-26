package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> findByUsername(String username);

    Usuario save(Usuario usuario);

    boolean existsByUsername(String username);
}