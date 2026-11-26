package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.PartidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidoJpaRepository extends JpaRepository<PartidoEntity, Long> {

    @Query("SELECT p FROM PartidoEntity p WHERE " +
            "(:idTorneo IS NULL OR p.idTorneo = :idTorneo) AND " +
            "(:estado IS NULL OR p.estado = :estado) AND " +
            "(:idEquipo IS NULL OR p.idEquipoLocal = :idEquipo OR p.idEquipoVisitante = :idEquipo)")
    Page<PartidoEntity> findByFiltros(@Param("idTorneo") Long idTorneo,
                                      @Param("idEquipo") Long idEquipo,
                                      @Param("estado") String estado,
                                      Pageable pageable);

    @Query("SELECT COUNT(p) > 0 FROM PartidoEntity p WHERE " +
            "(p.idEquipoLocal = :idEquipo OR p.idEquipoVisitante = :idEquipo) AND p.estado = 'JUGADO'")
    boolean hasPartidosJugados(@Param("idEquipo") Long idEquipo);
}