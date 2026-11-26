package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.out.UsuarioRepository;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AutenticacionService autenticacionService;

    @Test
    void login_credencialesValidas_devuelveToken() {
        Rol rol = new Rol(1, "ROLE_ADMIN", "Admin");
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("juan");
        usuario.setPassword("hashed");
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);

        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(usuario));
        when(jwtService.generarToken(usuario)).thenReturn("token-123");

        String token = autenticacionService.login("juan", "clave123");

        assertThat(token).isEqualTo("token-123");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generarToken(usuario);
    }

    @Test
    void login_usuarioNoEncontradoTrasAutenticar_lanzaReglaDeNegocioException() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autenticacionService.login("fantasma", "clave123"))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    void registrar_datosValidos_creaUsuarioConRolMapeado() {
        when(passwordEncoder.encode("clave123")).thenReturn("hashed");

        autenticacionService.registrar("nuevo", "clave123", "email@email.com", 2);

        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getUsername().equals("nuevo")
                        && usuario.getPassword().equals("hashed")
                        && usuario.getEstado().equals("ACTIVO")
                        && usuario.getRol().getIdRol() == 2
                        && usuario.getRol().getNombre().equals("ROLE_ARBITRO")));
    }

    @Test
    void registrar_idRolInvalido_lanzaIllegalArgumentException() {
        assertThatThrownBy(() -> autenticacionService.registrar("nuevo", "clave123", "email@email.com", 99))
                .isInstanceOf(IllegalArgumentException.class);

        verify(usuarioRepository, never()).save(any());
    }
}