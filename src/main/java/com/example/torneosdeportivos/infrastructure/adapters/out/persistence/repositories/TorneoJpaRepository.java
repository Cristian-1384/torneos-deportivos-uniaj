package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.TorneoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoJpaRepository extends JpaRepository<TorneoEntity, Long> {

    @Query("SELECT t FROM TorneoEntity t WHERE " +
            "(:disciplina IS NULL OR t.disciplina = :disciplina) AND " +
            "(:estado IS NULL OR t.estado = :estado)")
    Page<TorneoEntity> findByFiltros(@Param("disciplina") String disciplina,
                                     @Param("estado") String estado,
                                     Pageable pageable);
}
