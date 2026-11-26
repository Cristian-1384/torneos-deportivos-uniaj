package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;

import java.util.Optional;

public interface TorneoRepository {

    Wrapper<Torneo> getAll(int page, int size, String sortBy, String disciplina, String estado);

    Torneo save(Torneo torneo);

    Optional<Torneo> findById(Long idTorneo);

    Torneo update(Long idTorneo, Torneo torneo);
}