package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.TorneoRepository;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.TorneoEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.TorneoPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.TorneoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostgresqlAdapter implements TorneoRepository {

    private final TorneoJpaRepository torneoJpaRepository;

    public PostgresqlAdapter(TorneoJpaRepository torneoJpaRepository) {
        this.torneoJpaRepository = torneoJpaRepository;
    }

    @Override
    public Wrapper<Torneo> getAll(int page, int size, String sortBy, String disciplina, String estado) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Torneo> torneosPage = torneoJpaRepository.findByFiltros(disciplina, estado, pageable)
                .map(TorneoPersistenceMapper.INSTANCE::toDomain);

        Wrapper<Torneo> wrapper = new Wrapper<>();
        wrapper.setTotalElements((int) torneosPage.getTotalElements());
        wrapper.setTotalPages(torneosPage.getTotalPages());
        wrapper.setNumberOfElements(torneosPage.getNumberOfElements());
        wrapper.setSize(torneosPage.getSize());
        wrapper.setResults(torneosPage.getContent());
        return wrapper;
    }

    @Override
    public Torneo save(Torneo torneo) {
        TorneoEntity entity = TorneoPersistenceMapper.INSTANCE.toEntity(torneo);
        if (entity.getFechaCreacion() == null) {
            entity.setFechaCreacion(java.time.LocalDateTime.now());
        }
        TorneoEntity savedEntity = torneoJpaRepository.save(entity);
        return TorneoPersistenceMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public Optional<Torneo> findById(Long idTorneo) {
        return torneoJpaRepository.findById(idTorneo)
                .map(TorneoPersistenceMapper.INSTANCE::toDomain);
    }

    @Override
    public Torneo update(Long idTorneo, Torneo torneo) {
        TorneoEntity entityExistente = torneoJpaRepository.findById(idTorneo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encuentra el torneo con el id: " + idTorneo));

        entityExistente.setNombre(torneo.getNombre());
        entityExistente.setDisciplina(torneo.getDisciplina());
        entityExistente.setEstado(torneo.getEstado());

        if (torneo.getFechaInicio() != null) {
            entityExistente.setFechaInicio(torneo.getFechaInicio());
        }
        if (torneo.getFechaFin() != null) {
            entityExistente.setFechaFin(torneo.getFechaFin());
        }

        TorneoEntity savedEntity = torneoJpaRepository.save(entityExistente);
        return TorneoPersistenceMapper.INSTANCE.toDomain(savedEntity);
    }
}