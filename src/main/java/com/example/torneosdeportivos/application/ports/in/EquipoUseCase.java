package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;

public interface EquipoUseCase {

    Equipo crear(Equipo equipo);

    Wrapper<Equipo> listar(int page, int size, String sortBy, String estado, String nombre);

    Equipo buscarPorId(Long idEquipo);

    Equipo actualizar(Long idEquipo, Equipo equipo);

    void eliminarOInactivar(Long idEquipo);

    Wrapper<Jugador> consultarJugadores(Long idEquipo, int page, int size, String sortBy);
}