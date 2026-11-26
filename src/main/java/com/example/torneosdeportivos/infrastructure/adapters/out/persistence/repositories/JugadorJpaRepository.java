package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorJpaRepository extends JpaRepository<JugadorEntity, Long> {

    Page<JugadorEntity> findByIdEquipo(Long idEquipo, Pageable pageable);

    @Query("SELECT COUNT(j) FROM JugadorEntity j WHERE j.idEquipo = :idEquipo AND j.estado = 'ACTIVO'")
    long countJugadoresActivos(@Param("idEquipo") Long idEquipo);
}