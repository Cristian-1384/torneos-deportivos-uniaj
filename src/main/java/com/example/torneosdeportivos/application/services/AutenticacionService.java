package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.AutenticacionUseCase;
import com.example.torneosdeportivos.application.ports.out.UsuarioRepository;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AutenticacionService implements AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AutenticacionService(UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder,
                                AuthenticationManager authenticationManager,
                                JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ReglaDeNegocioException("Usuario no encontrado"));

        return jwtService.generarToken(usuario);
    }

    @Override
    public void registrar(String username, String password, String email, Integer idRol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new ReglaDeNegocioException("El username ya está en uso");
        }
        com.example.torneosdeportivos.domain.enums.Roles rolValido = com.example.torneosdeportivos.domain.enums.Roles.buscarPorId(idRol);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setEstado("ACTIVO");

        Rol rolDominio = new Rol();
        rolDominio.setIdRol(rolValido.getId());
        rolDominio.setNombre(rolValido.name());

        nuevoUsuario.setRol(rolDominio);

        nuevoUsuario.setFechaCreacion(LocalDateTime.now());

        usuarioRepository.save(nuevoUsuario);
    }
}