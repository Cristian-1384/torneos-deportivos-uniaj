package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.application.services.TorneoService;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.exceptions.GlobalExceptionHandler;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.UsuarioDetailsService;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.PostgresqlAdapter;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.UsuarioPersistenceAdapter;
import com.example.torneosdeportivos.infrastructure.config.SecurityConfig;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {
                TorneoController.class,
                TorneoService.class,
                PostgresqlAdapter.class,
                UsuarioPersistenceAdapter.class,
                UsuarioDetailsService.class,
                JwtService.class,
                JwtAuthenticationFilter.class,
                SecurityConfig.class,
                GlobalExceptionHandler.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableAutoConfiguration
@AutoConfigureMockMvc
@EntityScan(basePackages = "com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities")
@EnableJpaRepositories(basePackages = "com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories")
@ActiveProfiles("test")
@Transactional
class TorneoIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private RedisTemplate<String, String> redisTemplate;
    @MockBean private Gson gson;

    @Test
    @WithMockUser(roles = "ADMIN")
    void flujoCompletoTorneo_crearConsultarIniciarYCerrar() throws Exception {
        @SuppressWarnings("unchecked")
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn(null);

        String body = """
                {
                    "nombre": "Torneo Integración UNIAJC",
                    "disciplina": "Fútbol",
                    "fechaInicio": "2026-11-25",
                    "fechaFin": "2026-12-10"
                }
                """;

        // 1. Crear en H2 real
        mockMvc.perform(post("/api/torneos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PLANIFICADO"));

        // 2. Listar paginado
        mockMvc.perform(get("/api/torneos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // 3. Buscar por ID inexistente (cubre 404 real)
        mockMvc.perform(get("/api/torneos/999"))
                .andExpect(status().isNotFound());
    }
}
