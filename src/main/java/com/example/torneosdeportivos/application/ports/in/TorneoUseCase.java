package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;

public interface TorneoUseCase {

    Wrapper<Torneo> listar(int page, int size, String sortBy, String disciplina, String estado, boolean refresh);

    Torneo crear(Torneo torneo);

    Torneo buscarPorId(Long idTorneo);

    Torneo actualizar(Long idTorneo, Torneo torneo);

    Torneo iniciarTorneo(Long idTorneo);

    Torneo cerrarTorneo(Long idTorneo);
}