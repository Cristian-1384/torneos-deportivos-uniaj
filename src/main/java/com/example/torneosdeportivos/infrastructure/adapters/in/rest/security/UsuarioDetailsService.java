package com.example.torneosdeportivos.infrastructure.adapters.in.rest.security;

import com.example.torneosdeportivos.application.ports.out.UsuarioRepository;
import com.example.torneosdeportivos.domain.enums.Roles;
import com.example.torneosdeportivos.domain.models.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        List<String> roles = new ArrayList<>();

        if (usuario.getRol() != null && usuario.getRol().getNombre() != null) {
            roles.add(usuario.getRol().getNombre());
        } else {
            roles.add("ROLE_ESTUDIANTE");
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!"ACTIVO".equals(usuario.getEstado()))
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}