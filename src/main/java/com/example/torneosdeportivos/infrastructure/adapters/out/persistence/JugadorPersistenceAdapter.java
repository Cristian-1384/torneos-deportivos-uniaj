package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.JugadorRepository;
import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.JugadorPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.JugadorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JugadorPersistenceAdapter implements JugadorRepository {

    private final JugadorJpaRepository jugadorJpaRepository;

    public JugadorPersistenceAdapter(JugadorJpaRepository jugadorJpaRepository) {
        this.jugadorJpaRepository = jugadorJpaRepository;
    }

    @Override
    public com.example.torneosdeportivos.domain.models.Wrapper<Jugador> getByEquipo(Long idEquipo, int page, int size, String sortBy) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Jugador> jugadorPage = jugadorJpaRepository.findByIdEquipo(idEquipo, pageable)
                .map(JugadorPersistenceMapper::toDomain);

        com.example.torneosdeportivos.domain.models.Wrapper<Jugador> wrapper = new com.example.torneosdeportivos.domain.models.Wrapper<>();
        wrapper.setTotalElements((int) jugadorPage.getTotalElements());
        wrapper.setTotalPages(jugadorPage.getTotalPages());
        wrapper.setNumberOfElements(jugadorPage.getNumberOfElements());
        wrapper.setSize(jugadorPage.getSize());
        wrapper.setResults(jugadorPage.getContent());
        return wrapper;
    }

    @Override
    public Jugador save(Jugador jugador) {
        JugadorEntity entity = JugadorPersistenceMapper.toEntity(jugador);
        JugadorEntity savedEntity = jugadorJpaRepository.save(entity);
        return JugadorPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Jugador> findById(Long idJugador) {
        return jugadorJpaRepository.findById(idJugador)
                .map(JugadorPersistenceMapper::toDomain);
    }


    @Override
    public long countByEquipoId(Long idEquipo) {
        return jugadorJpaRepository.countJugadoresActivos(idEquipo);
    }
}