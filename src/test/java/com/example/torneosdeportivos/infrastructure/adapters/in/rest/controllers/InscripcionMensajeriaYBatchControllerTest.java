package com.example.torneosdeportivos.infrastructure.adapters.in.rest.controllers;

import com.example.torneosdeportivos.TestWebMvcConfiguration;
import com.example.torneosdeportivos.application.ports.in.InscripcionUseCase;
import com.example.torneosdeportivos.application.ports.in.JugadorMensajeriaUseCase;
import com.example.torneosdeportivos.application.ports.in.ReportesBatchUseCase;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {InscripcionController.class, JugadorMensajeriaController.class, ReportesBatchController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, InscripcionController.class, JugadorMensajeriaController.class, ReportesBatchController.class, TestWebMvcConfiguration.class})
class InscripcionMensajeriaYBatchControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private InscripcionUseCase inscripcionUseCase;
    @MockBean private JugadorMensajeriaUseCase jugadorMensajeriaUseCase;
    @MockBean private ReportesBatchUseCase reportesBatchUseCase;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void inscripcionMensajeriaYBatch_endpointsFuncionan() throws Exception {
        InscripcionJugador ins = new InscripcionJugador();
        ins.setIdInscripcion(1L);
        Wrapper<InscripcionJugador> wrapper = new Wrapper<>();
        wrapper.setResults(List.of(ins));

        when(inscripcionUseCase.inscribirJugador(any())).thenReturn(ins);
        when(inscripcionUseCase.listar(0, 10, "idInscripcion")).thenReturn(wrapper);
        when(inscripcionUseCase.retirarInscripcion(1L)).thenReturn(ins);

        String insBody = """
                {
                    "idJugador": 1,
                    "idTorneo": 1,
                    "dorsal": 10
                }
                """;

        mockMvc.perform(post("/api/inscripciones").contentType(MediaType.APPLICATION_JSON).content(insBody)).andExpect(status().isCreated());
        mockMvc.perform(get("/api/inscripciones")).andExpect(status().isOk());
        mockMvc.perform(put("/api/inscripciones/1/retirar")).andExpect(status().isOk());

        doNothing().when(jugadorMensajeriaUseCase).enviarRegistroACola(any());
        String msgBody = """
                {
                    "idEquipo": 1,
                    "cedula": "999",
                    "nombres": "Ana",
                    "apellidos": "Gomez",
                    "posicion": "Portera"
                }
                """;
        mockMvc.perform(post("/api/mensajeria/jugadores/registro").contentType(MediaType.APPLICATION_JSON).content(msgBody)).andExpect(status().isAccepted());

        doNothing().when(reportesBatchUseCase).ejecutarReporteTorneos();
        doNothing().when(reportesBatchUseCase).ejecutarReporteJugadoresInactivos();
        mockMvc.perform(post("/api/batch/reportes/torneos")).andExpect(status().isOk());
        mockMvc.perform(post("/api/batch/reportes/jugadores-inactivos")).andExpect(status().isOk());
    }
}
