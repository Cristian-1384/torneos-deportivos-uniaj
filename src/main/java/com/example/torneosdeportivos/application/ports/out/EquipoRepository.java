package com.example.torneosdeportivos.application.ports.out;

import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import java.util.Optional;

public interface EquipoRepository {
    Equipo save(Equipo equipo);
    Optional<Equipo> findById(Long idEquipo);
    boolean existsById(Long idEquipo);

    Wrapper<Equipo> getAll(int page, int size, String sortBy, String estado, String nombre);
}