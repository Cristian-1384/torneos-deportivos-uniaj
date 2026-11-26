package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.PartidoRepository;
import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.PartidoEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.PartidoPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.PartidoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PartidoPersistenceAdapter implements PartidoRepository {

    private final PartidoJpaRepository partidoJpaRepository;

    public PartidoPersistenceAdapter(PartidoJpaRepository partidoJpaRepository) {
        this.partidoJpaRepository = partidoJpaRepository;
    }

    @Override
    public Partido save(Partido partido) {
        PartidoEntity entity = PartidoPersistenceMapper.toEntity(partido);
        PartidoEntity savedEntity = partidoJpaRepository.save(entity);
        return PartidoPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Partido> findById(Long idPartido) {
        return partidoJpaRepository.findById(idPartido)
                .map(PartidoPersistenceMapper::toDomain);
    }

    @Override
    public com.example.torneosdeportivos.domain.models.Wrapper<Partido> getAll(int page, int size, String sortBy, Long idTorneo, Long idEquipo, String estado) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Partido> partidoPage = partidoJpaRepository.findByFiltros(idTorneo, idEquipo, estado, pageable)
                .map(PartidoPersistenceMapper::toDomain);

        com.example.torneosdeportivos.domain.models.Wrapper<Partido> wrapper = new com.example.torneosdeportivos.domain.models.Wrapper<>();
        wrapper.setTotalElements((int) partidoPage.getTotalElements());
        wrapper.setTotalPages(partidoPage.getTotalPages());
        wrapper.setNumberOfElements(partidoPage.getNumberOfElements());
        wrapper.setSize(partidoPage.getSize());
        wrapper.setResults(partidoPage.getContent());
        return wrapper;
    }

    @Override
    public boolean hasPartidosJugados(Long idEquipo) {
        return partidoJpaRepository.hasPartidosJugados(idEquipo);
    }
}