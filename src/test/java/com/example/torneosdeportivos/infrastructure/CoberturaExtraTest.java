package com.example.torneosdeportivos.infrastructure;

import com.example.torneosdeportivos.domain.models.Rol;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.exceptions.GlobalExceptionHandler;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.UsuarioPersistenceAdapter;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.RolEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.UsuarioPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoberturaExtraTest {

    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;
    @Mock private UsuarioJpaRepository usuarioJpaRepository;

    @Test
    void jwtFilter_conTokenBearerValido_autenticaEnContexto() throws Exception {
        SecurityContextHolder.clearContext();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);

        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido-123");
        when(jwtService.extraerUsername("token-valido-123")).thenReturn("cristian");
        when(jwtService.esTokenValido("token-valido-123", "cristian")).thenReturn(true);
        when(jwtService.extraerRoles("token-valido-123")).thenReturn(List.of("ROLE_ADMIN"));

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("cristian");
        verify(filterChain).doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

    @Test
    void usuarioPersistenceYMappers_cubrenTodasLasRamas() {
        UsuarioPersistenceAdapter adapter = new UsuarioPersistenceAdapter(usuarioJpaRepository);

        RolEntity rolEntity = new RolEntity(1L, "ROLE_ADMIN", "Admin");
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1L);
        entity.setUsername("cristian");
        entity.setRol(rolEntity);

        when(usuarioJpaRepository.findByUsername("cristian")).thenReturn(Optional.of(entity));
        when(usuarioJpaRepository.existsByUsername("cristian")).thenReturn(true);
        when(usuarioJpaRepository.save(any())).thenReturn(entity);

        Usuario u = new Usuario();
        u.setUsername("cristian");
        u.setRol(new Rol(1, "ROLE_ADMIN", "Admin")); // Si idRol es Long usa 1L

        assertThat(adapter.findByUsername("cristian")).isPresent();
        assertThat(adapter.existsByUsername("cristian")).isTrue();
        assertThat(adapter.save(u).getUsername()).isEqualTo("cristian");

        assertThat(UsuarioPersistenceMapper.toEntity(null)).isNull();
        assertThat(UsuarioPersistenceMapper.toDomain(null)).isNull();
    }

    @Test
    void globalExceptionHandler_validationException_devuelve400() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(request.getRequestURI()).thenReturn("/api/test");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("obj", "campo", "es obligatorio")));

        var res = handler.handleValidation(ex, request);
        assertThat(res.getStatusCode().value()).isEqualTo(400);
    }
}
