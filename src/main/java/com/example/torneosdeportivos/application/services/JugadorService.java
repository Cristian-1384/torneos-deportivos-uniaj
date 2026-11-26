package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.JugadorUseCase;
import com.example.torneosdeportivos.application.ports.out.JugadorRepository;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import org.springframework.stereotype.Service;

@Service
public class JugadorService implements JugadorUseCase {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    public Jugador crear(Jugador jugador) {
        jugador.setEstado("ACTIVO");
        return jugadorRepository.save(jugador);
    }

    @Override
    public Wrapper<Jugador> listar(int page, int size, String sortBy) {
        throw new UnsupportedOperationException("Listado general opcional; use consultarJugadores por equipo.");
    }

    @Override
    public Jugador buscarPorId(Long idJugador) {
        return jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador con ID " + idJugador + " no encontrado."));
    }

    @Override
    public Jugador actualizar(Long idJugador, Jugador jugador) {
        Jugador existente = buscarPorId(idJugador);
        existente.setNombres(jugador.getNombres());
        existente.setApellidos(jugador.getApellidos());
        existente.setPosicion(jugador.getPosicion());
        return jugadorRepository.save(existente);
    }

    @Override
    public void eliminar(Long idJugador) {
        Jugador jugador = buscarPorId(idJugador);
        jugador.setEstado("INACTIVO");
        jugadorRepository.save(jugador);
    }
}