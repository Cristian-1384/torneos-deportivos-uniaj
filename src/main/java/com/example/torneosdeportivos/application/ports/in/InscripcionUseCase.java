package com.example.torneosdeportivos.application.ports.in;

import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Wrapper;

public interface InscripcionUseCase {

    InscripcionJugador inscribirJugador(InscripcionJugador inscripcion);

    Wrapper<InscripcionJugador> listar(int page, int size, String sortBy);

    InscripcionJugador retirarInscripcion(Long idInscripcion);
}
