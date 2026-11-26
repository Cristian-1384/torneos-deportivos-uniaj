package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.InscripcionUseCase;
import com.example.torneosdeportivos.application.ports.out.InscripcionRepository;
import com.example.torneosdeportivos.application.ports.out.EquipoRepository;
import com.example.torneosdeportivos.application.ports.out.JugadorRepository;
import com.example.torneosdeportivos.domain.exceptions.CupoExcedidoException;
import com.example.torneosdeportivos.domain.exceptions.InscripcionDuplicadaException;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InscripcionService implements InscripcionUseCase {

    private final InscripcionRepository inscripcionRepository;
    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository,
                              JugadorRepository jugadorRepository,
                              EquipoRepository equipoRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public InscripcionJugador inscribirJugador(InscripcionJugador inscripcion) {
        Jugador jugador = jugadorRepository.findById(inscripcion.getIdJugador())
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador no encontrado"));

        Equipo equipo = equipoRepository.findById(jugador.getIdEquipo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado"));

        if (inscripcionRepository.existsByIdJugadorAndIdTorneo(inscripcion.getIdJugador(), inscripcion.getIdTorneo())) {
            throw new InscripcionDuplicadaException("El jugador ya está inscrito en este torneo.");
        }

        long jugadoresActivos = jugadorRepository.countByEquipoId(jugador.getIdEquipo());
        if (jugadoresActivos >= equipo.getCupoMaximoJugadores()) {
            throw new CupoExcedidoException("El equipo " + equipo.getNombre() + " ha alcanzado su cupo máximo de " + equipo.getCupoMaximoJugadores() + " jugadores.");
        }

        if (inscripcionRepository.existsByTorneoAndEquipoAndDorsal(inscripcion.getIdTorneo(), jugador.getIdEquipo(), inscripcion.getDorsal())) {
            throw new ReglaDeNegocioException("El dorsal " + inscripcion.getDorsal() + " ya está ocupado en este equipo para este torneo.");
        }

        inscripcion.setEstado("INSCRITO");
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public Wrapper<InscripcionJugador> listar(int page, int size, String sortBy) {
        return inscripcionRepository.getAll(page, size, sortBy);
    }

    @Override
    public InscripcionJugador retirarInscripcion(Long idInscripcion) {
        InscripcionJugador inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada"));
        inscripcion.setEstado("RETIRADO");
        return inscripcionRepository.save(inscripcion);
    }
}