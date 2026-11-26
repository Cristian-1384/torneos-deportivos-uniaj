package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.InscripcionRepository;
import com.example.torneosdeportivos.domain.models.InscripcionJugador;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.InscripcionJugadorEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.InscripcionPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.InscripcionJugadorJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InscripcionPersistenceAdapter implements InscripcionRepository {

    private final InscripcionJugadorJpaRepository jpaRepository;

    public InscripcionPersistenceAdapter(InscripcionJugadorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public InscripcionJugador save(InscripcionJugador inscripcion) {
        InscripcionJugadorEntity entity = InscripcionPersistenceMapper.toEntity(inscripcion);
        if (entity.getFechaInscripcion() == null) {
            entity.setFechaInscripcion(LocalDateTime.now());
        }
        InscripcionJugadorEntity savedEntity = jpaRepository.save(entity);
        return InscripcionPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<InscripcionJugador> findById(Long idInscripcion) {
        return jpaRepository.findById(idInscripcion)
                .map(InscripcionPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByIdJugadorAndIdTorneo(Long idJugador, Long idTorneo) {
        return jpaRepository.existsByIdJugadorAndIdTorneo(idJugador, idTorneo);
    }

    @Override
    public com.example.torneosdeportivos.domain.models.Wrapper<com.example.torneosdeportivos.domain.models.InscripcionJugador> getAll(int page, int size, String sortBy) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<com.example.torneosdeportivos.domain.models.InscripcionJugador> inscripcionPage = jpaRepository.findAll(pageable)
                .map(InscripcionPersistenceMapper::toDomain);

        Wrapper<InscripcionJugador> wrapper = createWrapper(inscripcionPage);
        return wrapper;
    }

    private static Wrapper<InscripcionJugador> createWrapper(Page<InscripcionJugador> inscripcionPage) {
        Wrapper<InscripcionJugador> wrapper = new Wrapper<>();
        wrapper.setTotalElements((int) inscripcionPage.getTotalElements());
        wrapper.setTotalPages(inscripcionPage.getTotalPages());
        wrapper.setNumberOfElements(inscripcionPage.getNumberOfElements());
        wrapper.setSize(inscripcionPage.getSize());
        wrapper.setResults(inscripcionPage.getContent());
        return wrapper;
    }

    @Override
    public boolean existsByTorneoAndEquipoAndDorsal(Long idTorneo, Long idEquipo, Integer dorsal) {
        return jpaRepository.existsDorsalDuplicadoEnEquipoYTorneo(idTorneo, idEquipo, dorsal);
    }
}