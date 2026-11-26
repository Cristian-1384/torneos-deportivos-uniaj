package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.CreateTorneoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.TorneoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TorneoMapper {

    TorneoMapper INSTANCE = Mappers.getMapper(TorneoMapper.class);

    TorneoResponse toTorneoResponse(Torneo torneo);

    Torneo toTorneo(CreateTorneoRequest torneo);
}
