package com.example.torneosdeportivos.infrastructure;

import com.example.torneosdeportivos.application.ports.out.UsuarioRepository;
import com.example.torneosdeportivos.domain.exceptions.*;
import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.exceptions.GlobalExceptionHandler;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityYExcepcionesTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @Test
    void jwtService_generaYValidaToken() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "secreto-super-largo-para-pruebas-unitarias-de-256-bits-minimo");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        Usuario usuario = new Usuario();
        usuario.setUsername("cristian");
        usuario.setRol(new Rol(1, "ROLE_ADMIN", "Admin")); // Si idRol es Long usa 1L

        String token = jwtService.generarToken(usuario);
        assertThat(token).isNotBlank();
        assertThat(jwtService.extraerUsername(token)).isEqualTo("cristian");

        assertThat(jwtService.esTokenValido(token, "cristian")).isTrue();
    }

    @Test
    void usuarioDetailsService_cargaUsuarioOLanzaExcepcion() {
        UsuarioDetailsService service = new UsuarioDetailsService(usuarioRepository);
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword("pass");
        usuario.setEstado("ACTIVO");
        usuario.setRol(new Rol(1, "ROLE_ADMIN", "Admin")); // Si idRol es Long usa 1L

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        UserDetails details = service.loadUserByUsername("admin");
        assertThat(details.getUsername()).isEqualTo("admin");

        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.loadUserByUsername("noexiste"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void jwtFilter_sinHeader_continuaCadena() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        UsuarioDetailsService detailsService = mock(UsuarioDetailsService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);

        when(request.getHeader("Authorization")).thenReturn(null);
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void globalExceptionHandler_cubreTodosLosCodigosHttp() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/test");

        assertThat(handler.handleAuthenticationException(new BadCredentialsException("Error"), request).getStatusCode().value()).isEqualTo(401);
        assertThat(handler.handleAccessDenied(new AccessDeniedException("Denegado"), request).getStatusCode().value()).isEqualTo(403);
        assertThat(handler.handleNotFound(new RecursoNoEncontradoException("No existe"), request).getStatusCode().value()).isEqualTo(404);
        assertThat(handler.handleBusinessRule(new ReglaDeNegocioException("Regla"), request).getStatusCode().value()).isEqualTo(409);
        assertThat(handler.handleGeneric(new RuntimeException("Fallo"), request).getStatusCode().value()).isEqualTo(500);

        // Excepciones de dominio
        assertThat(new CupoExcedidoException("Cupo")).hasMessage("Cupo");
        assertThat(new InscripcionDuplicadaException("Dup")).hasMessage("Dup");
        assertThat(new TorneoNoDisponibleException("No disp")).hasMessage("No disp");
    }
}
