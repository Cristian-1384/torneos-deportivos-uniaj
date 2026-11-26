package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.InscripcionJugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionJugadorJpaRepository extends JpaRepository<InscripcionJugadorEntity, Long> {

    boolean existsByIdJugadorAndIdTorneo(Long idJugador, Long idTorneo);

    @Query("SELECT COUNT(i) > 0 FROM InscripcionJugadorEntity i WHERE " +
            "i.idTorneo = :idTorneo AND " +
            "i.dorsal = :dorsal AND " +
            "EXISTS (SELECT j FROM JugadorEntity j WHERE j.idJugador = i.idJugador AND j.idEquipo = :idEquipo)")
    boolean existsDorsalDuplicadoEnEquipoYTorneo(@Param("idTorneo") Long idTorneo,
                                                 @Param("idEquipo") Long idEquipo,
                                                 @Param("dorsal") Integer dorsal);
}
