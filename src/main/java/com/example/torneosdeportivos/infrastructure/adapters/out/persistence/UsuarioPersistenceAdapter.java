package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.UsuarioRepository;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioJpaRepository.findByUsername(username)
                .map(com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.UsuarioPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioJpaRepository.existsByUsername(username);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.UsuarioPersistenceMapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioJpaRepository.save(entity);
        return com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.UsuarioPersistenceMapper.toDomain(savedEntity);
    }
}
