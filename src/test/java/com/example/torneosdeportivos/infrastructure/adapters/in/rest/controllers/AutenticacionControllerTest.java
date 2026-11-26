package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.TestWebMvcConfiguration;
import com.example.torneosdeportivos.application.ports.in.AutenticacionUseCase;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtAuthenticationFilter;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.security.JwtService;
import com.example.torneosdeportivos.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AutenticacionController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, AutenticacionController.class, TestWebMvcConfiguration.class})
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticacionUseCase autenticacionUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void login_credencialesValidas_devuelve200ConToken() throws Exception {
        when(autenticacionUseCase.login("admin", "clave123")).thenReturn("token-jwt-valido");

        String body = """
                {
                    "username": "admin",
                    "password": "clave123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token-jwt-valido"));
    }

    @Test
    void login_datosVacios_devuelve400() throws Exception {
        String body = """
                {
                    "username": "",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrar_datosValidosConRol_devuelve201() throws Exception {
        doNothing().when(autenticacionUseCase).registrar("arbitro1", "clave123", "arb@uniajc.edu.co", 2);

        String body = """
                {
                    "username": "arbitro1",
                    "password": "clave123",
                    "email": "arb@uniajc.edu.co",
                    "idRol": 2
                }
                """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(autenticacionUseCase).registrar("arbitro1", "clave123", "arb@uniajc.edu.co", 2);
    }
}