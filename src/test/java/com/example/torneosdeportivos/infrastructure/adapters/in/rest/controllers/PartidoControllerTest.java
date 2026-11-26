package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.TestWebMvcConfiguration;
import com.example.torneosdeportivos.application.ports.in.PartidoUseCase;
import com.example.torneosdeportivos.domain.models.Partido;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PartidoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, PartidoController.class, TestWebMvcConfiguration.class})
class PartidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidoUseCase partidoUseCase;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ARBITRO")
    void registrarResultado_devuelve200() throws Exception {
        Partido partido = new Partido();
        partido.setIdPartido(1L);
        partido.setMarcadorLocal(2);
        partido.setMarcadorVisitante(1);
        partido.setEstado("JUGADO");

        when(partidoUseCase.registrarResultado(1L, 2, 1)).thenReturn(partido);

        String body = """
                {
                    "marcadorLocal": 2,
                    "marcadorVisitante": 1
                }
                """;

        mockMvc.perform(put("/api/partidos/1/resultado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("JUGADO"))
                .andExpect(jsonPath("$.marcadorLocal").value(2));
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void consultarTablaPosiciones_devuelve200ConLista() throws Exception {
        List<Map<String, Object>> tabla = List.of(
                Map.of("equipo", "Sistemas FC", "pts", 3, "pj", 1)
        );

        when(partidoUseCase.consultarTablaPosiciones(1L)).thenReturn(tabla);

        mockMvc.perform(get("/api/partidos/torneo/1/tabla-posiciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].equipo").value("Sistemas FC"))
                .andExpect(jsonPath("$[0].pts").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void programarListarYAplazarPartido_devuelven200Y201() throws Exception {
        Partido p = new Partido();
        p.setIdPartido(1L);
        p.setEstado("PROGRAMADO");

        com.example.torneosdeportivos.domain.models.Wrapper<Partido> wrapper = new com.example.torneosdeportivos.domain.models.Wrapper<>();
        wrapper.setResults(List.of(p));

        when(partidoUseCase.programarPartido(any())).thenReturn(p);
        when(partidoUseCase.listar(0, 10, "idPartido", null, null, null)).thenReturn(wrapper);
        when(partidoUseCase.aplazarPartido(1L)).thenReturn(p);

        String body = """
                {
                    "idTorneo": 1,
                    "idEquipoLocal": 1,
                    "idEquipoVisitante": 2,
                    "fechaPartido": "2026-11-25T15:00:00"
                }
                """;

        mockMvc.perform(post("/api/partidos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/partidos"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/partidos/1/aplazar"))
                .andExpect(status().isOk());
    }
}
