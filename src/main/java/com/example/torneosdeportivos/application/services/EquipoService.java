package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.EquipoUseCase;
import com.example.torneosdeportivos.application.ports.out.EquipoRepository;
import com.example.torneosdeportivos.application.ports.out.JugadorRepository;
import com.example.torneosdeportivos.application.ports.out.PartidoRepository;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EquipoService implements EquipoUseCase {

    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final PartidoRepository partidoRepository;

    public EquipoService(EquipoRepository equipoRepository,
                         JugadorRepository jugadorRepository,
                         PartidoRepository partidoRepository) {
        this.equipoRepository = equipoRepository;
        this.jugadorRepository = jugadorRepository;
        this.partidoRepository = partidoRepository;
    }

    @Override
    public Equipo crear(Equipo equipo) {
        equipo.setEstado("ACTIVO");
        equipo.setFechaCreacion(LocalDateTime.now());
        return equipoRepository.save(equipo);
    }

    @Override
    public Wrapper<Equipo> listar(int page, int size, String sortBy, String estado, String nombre) {
        return equipoRepository.getAll(page, size, sortBy, estado, nombre);
    }

    @Override
    public Equipo buscarPorId(Long idEquipo) {
        return equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo con ID " + idEquipo + " no encontrado."));
    }

    @Override
    public Equipo actualizar(Long idEquipo, Equipo equipo) {
        Equipo existente = buscarPorId(idEquipo);
        existente.setNombre(equipo.getNombre());
        existente.setFacultadPrograma(equipo.getFacultadPrograma());
        existente.setCupoMaximoJugadores(equipo.getCupoMaximoJugadores());
        return equipoRepository.save(existente);
    }

    @Override
    public void eliminarOInactivar(Long idEquipo) {
        Equipo equipo = buscarPorId(idEquipo);

        if (partidoRepository.hasPartidosJugados(idEquipo)) {
            equipo.setEstado("INACTIVO");
            equipoRepository.save(equipo);
            throw new ReglaDeNegocioException("El equipo tiene partidos disputados. No se puede eliminar, ha sido cambiado a INACTIVO.");
        }

        equipo.setEstado("INACTIVO");
        equipoRepository.save(equipo);
    }

    @Override
    public Wrapper<Jugador> consultarJugadores(Long idEquipo, int page, int size, String sortBy) {
        buscarPorId(idEquipo);
        return jugadorRepository.getByEquipo(idEquipo, page, size, sortBy);
    }
}