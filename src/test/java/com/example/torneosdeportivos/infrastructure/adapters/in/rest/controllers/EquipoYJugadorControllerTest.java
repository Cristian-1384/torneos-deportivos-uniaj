package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.TestWebMvcConfiguration;
import com.example.torneosdeportivos.application.ports.in.EquipoUseCase;
import com.example.torneosdeportivos.application.ports.in.JugadorUseCase;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Jugador;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {EquipoController.class, JugadorController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, EquipoController.class, JugadorController.class, TestWebMvcConfiguration.class})
class EquipoYJugadorControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private EquipoUseCase equipoUseCase;
    @MockBean private JugadorUseCase jugadorUseCase;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void equipoController_crudCompleto() throws Exception {
        Equipo eq = new Equipo();
        eq.setIdEquipo(1L);
        eq.setNombre("Sistemas FC");

        Wrapper<Equipo> wrapperEq = new Wrapper<>();
        wrapperEq.setTotalElements(1);
        wrapperEq.setResults(List.of(eq));

        Wrapper<Jugador> wrapperJug = new Wrapper<>();
        wrapperJug.setTotalElements(1);
        wrapperJug.setResults(List.of(new Jugador()));

        when(equipoUseCase.crear(any())).thenReturn(eq);
        when(equipoUseCase.listar(0, 10, "idEquipo", null, null)).thenReturn(wrapperEq);
        when(equipoUseCase.buscarPorId(1L)).thenReturn(eq);
        when(equipoUseCase.actualizar(eq(1L), any())).thenReturn(eq);
        when(equipoUseCase.consultarJugadores(1L, 0, 10, "idJugador")).thenReturn(wrapperJug);
        doNothing().when(equipoUseCase).eliminarOInactivar(1L);

        String body = """
                {
                    "nombre": "Sistemas FC",
                    "facultadPrograma": "Ingeniería",
                    "cupoMaximoJugadores": 12
                }
                """;

        mockMvc.perform(post("/api/equipos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sistemas FC"));

        mockMvc.perform(get("/api/equipos")).andExpect(status().isOk());
        mockMvc.perform(get("/api/equipos/1")).andExpect(status().isOk());
        mockMvc.perform(put("/api/equipos/1").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
        mockMvc.perform(get("/api/equipos/1/jugadores")).andExpect(status().isOk());
        mockMvc.perform(delete("/api/equipos/1")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void jugadorController_crudCompleto() throws Exception {
        Jugador jug = new Jugador();
        jug.setIdJugador(1L);
        jug.setNombres("Carlos");

        when(jugadorUseCase.crear(any())).thenReturn(jug);
        when(jugadorUseCase.buscarPorId(1L)).thenReturn(jug);
        when(jugadorUseCase.actualizar(eq(1L), any())).thenReturn(jug);
        doNothing().when(jugadorUseCase).eliminar(1L);

        String body = """
                {
                    "idEquipo": 1,
                    "cedula": "123456",
                    "nombres": "Carlos",
                    "apellidos": "Pérez",
                    "posicion": "Delantero"
                }
                """;

        mockMvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/jugadores/1")).andExpect(status().isOk());
        mockMvc.perform(put("/api/jugadores/1").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/jugadores/1")).andExpect(status().isNoContent());
    }
}
