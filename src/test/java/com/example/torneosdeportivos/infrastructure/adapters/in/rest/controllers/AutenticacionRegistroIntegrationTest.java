package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.services.AutenticacionService;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.TokenResponse;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.exceptions.GlobalExceptionHandler;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.UsuarioDetailsService;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.UsuarioPersistenceAdapter;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.RolEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.RolJpaRepository;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.UsuarioJpaRepository;
import com.example.torneosdeportivos.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {
                AutenticacionController.class,
                AutenticacionService.class,
                UsuarioPersistenceAdapter.class,
                UsuarioDetailsService.class,
                JwtService.class,
                JwtAuthenticationFilter.class,
                SecurityConfig.class,
                GlobalExceptionHandler.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableAutoConfiguration
@AutoConfigureMockMvc
@EntityScan(basePackages = "com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities")
@EnableJpaRepositories(basePackages = "com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories")
@ActiveProfiles("test")
@Transactional
class AutenticacionRegistroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RolJpaRepository rolJpaRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpRoles() {
        jdbcTemplate.execute("DELETE FROM usuario");
        jdbcTemplate.execute("DELETE FROM rol");
        jdbcTemplate.execute("ALTER TABLE rol ALTER COLUMN id_rol RESTART WITH 1");

        rolJpaRepository.saveAndFlush(new RolEntity(null, "ROLE_ADMIN", "Administrador"));
        rolJpaRepository.saveAndFlush(new RolEntity(null, "ROLE_ARBITRO", "Árbitro"));
        rolJpaRepository.saveAndFlush(new RolEntity(null, "ROLE_ESTUDIANTE", "Estudiante"));
    }

    @Test
    void registrar_conDatosValidos_persisteUsuarioConPasswordHasheadoYRolEstudiante() throws Exception {
        String body = """
                {
                    "username": "integracion1",
                    "password": "clave123",
                    "email": "email@email.com",
                    "idRol": 3
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        assertThat(usuarioJpaRepository.existsByUsername("integracion1")).isTrue();

        var entity = usuarioJpaRepository.findByUsername("integracion1").orElseThrow();
        assertThat(entity.getEstado()).isEqualTo("ACTIVO");
        assertThat(entity.getRol().getIdRol()).isEqualTo(3L);
        assertThat(entity.getPassword()).isNotEqualTo("clave123");
        assertThat(passwordEncoder.matches("clave123", entity.getPassword())).isTrue();
    }

    @Test
    void registrar_usernameYaRegistrado_devuelve409() throws Exception {
        String body = """
                {
                    "username": "duplicado",
                    "password": "clave123",
                    "email": "email@email.com",
                    "idRol": 3
                }
                """;
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());

        assertThat(usuarioJpaRepository.findByUsername("duplicado")).isPresent();
    }

    @Test
    void registrar_passwordMuyCorto_devuelve400YNoPersiste() throws Exception {
        String body = """
                {
                    "username": "passwordcorto",
                    "password": "123",
                    "email": "email@email.com",
                    "idRol": 3
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        assertThat(usuarioJpaRepository.existsByUsername("passwordcorto")).isFalse();
    }

    @Test
    void registrar_usernameVacio_devuelve400YNoPersiste() throws Exception {
        String body = """
                {
                    "username": "",
                    "password": "clave123",
                    "email": "email@email.com",
                    "idRol": 3
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarYLuegoLogin_devuelveTokenJwtValido() throws Exception {
        String registroBody = """
                {
                    "username": "flujoCompleto",
                    "password": "clave123",
                    "email": "email@email.com",
                    "idRol": 3
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registroBody))
                .andExpect(status().isCreated());

        String loginBody = """
                {
                    "username": "flujoCompleto",
                    "password": "clave123"
                }
                """;

        String responseJson = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn().getResponse().getContentAsString();

        TokenResponse token = objectMapper.readValue(responseJson, TokenResponse.class);
        assertThat(token.getAccessToken()).isNotBlank();
    }
}
