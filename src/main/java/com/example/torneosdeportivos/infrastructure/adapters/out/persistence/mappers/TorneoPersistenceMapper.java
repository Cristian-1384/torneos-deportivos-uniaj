package com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers;

import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.TorneoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface TorneoPersistenceMapper {

    TorneoPersistenceMapper INSTANCE = Mappers.getMapper(TorneoPersistenceMapper.class);

    TorneoEntity toEntity(Torneo torneo);

    Torneo toDomain(TorneoEntity torneoEntity);

    default LocalDate mapDateToLocalDate(Date date) {
        if (date == null) return null;
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    default Date mapLocalDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return java.sql.Date.valueOf(localDate);
    }

    default LocalDateTime mapDateToLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    default Date mapLocalDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
