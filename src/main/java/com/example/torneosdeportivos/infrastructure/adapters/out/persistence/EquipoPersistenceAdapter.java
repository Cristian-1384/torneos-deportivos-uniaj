package com.example.torneosdeportivos.infrastructure.adapters.out.persistence;

import com.example.torneosdeportivos.application.ports.out.EquipoRepository; // Ajusta según tu nombre de puerto
import com.example.torneosdeportivos.domain.models.Equipo;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.EquipoEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.EquipoPersistenceMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.EquipoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EquipoPersistenceAdapter implements EquipoRepository{

    private final EquipoJpaRepository equipoJpaRepository;

    public EquipoPersistenceAdapter(EquipoJpaRepository equipoJpaRepository) {
        this.equipoJpaRepository = equipoJpaRepository;
    }

    @Override
    public Equipo save(Equipo equipo) {
        EquipoEntity entity = EquipoPersistenceMapper.toEntity(equipo);
        if (entity.getFechaCreacion() == null) {
            entity.setFechaCreacion(LocalDateTime.now());
        }
        EquipoEntity savedEntity = equipoJpaRepository.save(entity);
        return EquipoPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public com.example.torneosdeportivos.domain.models.Wrapper<Equipo> getAll(int page, int size, String sortBy, String estado, String nombre) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Equipo> equipoPage = equipoJpaRepository.findByFiltros(estado, nombre, pageable)
                .map(EquipoPersistenceMapper::toDomain);

        com.example.torneosdeportivos.domain.models.Wrapper<Equipo> wrapper = new com.example.torneosdeportivos.domain.models.Wrapper<>();
        wrapper.setTotalElements((int) equipoPage.getTotalElements());
        wrapper.setTotalPages(equipoPage.getTotalPages());
        wrapper.setNumberOfElements(equipoPage.getNumberOfElements());
        wrapper.setSize(equipoPage.getSize());
        wrapper.setResults(equipoPage.getContent());
        return wrapper;
    }

    @Override
    public Optional<Equipo> findById(Long idEquipo) {
        return equipoJpaRepository.findById(idEquipo)
                .map(EquipoPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(Long idEquipo) {
        return equipoJpaRepository.existsById(idEquipo);
    }
}