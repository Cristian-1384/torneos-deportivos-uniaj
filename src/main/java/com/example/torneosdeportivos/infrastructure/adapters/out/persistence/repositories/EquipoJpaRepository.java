package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.EquipoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoJpaRepository extends JpaRepository<EquipoEntity, Long> {

    @Query("SELECT e FROM EquipoEntity e WHERE " +
            "(:estado IS NULL OR e.estado = :estado) AND " +
            "(:nombre IS NULL OR UPPER(e.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')))")
    Page<EquipoEntity> findByFiltros(@Param("estado") String estado,
                                     @Param("nombre") String nombre,
                                     Pageable pageable);
}