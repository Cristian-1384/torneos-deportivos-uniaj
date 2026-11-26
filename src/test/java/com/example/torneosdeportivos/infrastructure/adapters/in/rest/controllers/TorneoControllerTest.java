package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.TestWebMvcConfiguration;
import com.example.torneosdeportivos.application.ports.in.TorneoUseCase;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TorneoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, TorneoController.class, TestWebMvcConfiguration.class})
class TorneoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TorneoUseCase torneoUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearTorneo_datosValidos_devuelve201() throws Exception {
        Torneo creado = new Torneo();
        creado.setIdTorneo(1L);
        creado.setNombre("Copa UNIAJC");
        creado.setDisciplina("Fútbol");
        creado.setEstado("PLANIFICADO");

        when(torneoUseCase.crear(any(Torneo.class))).thenReturn(creado);

        String body = """
                {
                    "nombre": "Copa UNIAJC",
                    "disciplina": "Fútbol",
                    "fechaInicio": "2026-11-25",
                    "fechaFin": "2026-12-10"
                }
                """;

        mockMvc.perform(post("/api/torneos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Copa UNIAJC"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cerrarTorneo_devuelve200() throws Exception {
        Torneo cerrado = new Torneo();
        cerrado.setIdTorneo(1L);
        cerrado.setEstado("FINALIZADO");

        when(torneoUseCase.cerrarTorneo(1L)).thenReturn(cerrado);

        mockMvc.perform(put("/api/torneos/1/cerrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADO"));
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void listarTorneos_devuelve200ConPaginacion() throws Exception {
        Wrapper<Torneo> wrapper = new Wrapper<>();
        wrapper.setTotalElements(1);
        wrapper.setResults(List.of(new Torneo()));

        when(torneoUseCase.listar(0, 10, "idTorneo", null, null, false)).thenReturn(wrapper);

        mockMvc.perform(get("/api/torneos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void iniciarTorneo_devuelve200() throws Exception {
        Torneo iniciado = new Torneo();
        iniciado.setIdTorneo(1L);
        iniciado.setEstado("EN_CURSO");

        when(torneoUseCase.iniciarTorneo(1L)).thenReturn(iniciado);

        mockMvc.perform(put("/api/torneos/1/iniciar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_CURSO"));
    }
}
