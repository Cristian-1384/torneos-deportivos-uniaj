package com.example.torneosdeportivos.infrastructure;

import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.CreateTorneoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers.TorneoMapper;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.TorneoEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.TorneoPersistenceMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CoberturaFinalTest {

    @Test
    void torneoMappers_cubrenTodasLasRamas() {
        CreateTorneoRequest req = new CreateTorneoRequest();
        req.setNombre("Copa UNIAJC");
        req.setDisciplina("Fútbol");
        req.setFechaInicio(LocalDate.now());
        req.setFechaFin(LocalDate.now().plusDays(10));

        Torneo domain = TorneoMapper.INSTANCE.toTorneo(req);
        assertThat(domain.getNombre()).isEqualTo("Copa UNIAJC");
        assertThat(TorneoMapper.INSTANCE.toTorneo(null)).isNull();

        domain.setIdTorneo(1L);
        domain.setEstado("EN_CURSO");
        domain.setFechaCreacion(LocalDateTime.now());

        var res = TorneoMapper.INSTANCE.toTorneoResponse(domain);
        assertThat(res.getIdTorneo()).isEqualTo(1L);
        assertThat(TorneoMapper.INSTANCE.toTorneoResponse(null)).isNull();

        TorneoEntity entity = TorneoPersistenceMapper.INSTANCE.toEntity(domain);
        assertThat(TorneoPersistenceMapper.INSTANCE.toDomain(entity).getNombre()).isEqualTo("Copa UNIAJC");
        assertThat(TorneoPersistenceMapper.INSTANCE.toEntity(null)).isNull();
        assertThat(TorneoPersistenceMapper.INSTANCE.toDomain(null)).isNull();
    }
}
