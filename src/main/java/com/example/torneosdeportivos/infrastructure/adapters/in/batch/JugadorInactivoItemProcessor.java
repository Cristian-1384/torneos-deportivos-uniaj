package com.example.torneosdeportivos.infrastructure.adapters.in.batch;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.mappers.JugadorPersistenceMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class JugadorInactivoItemProcessor implements ItemProcessor<JugadorEntity, Jugador> {

    @Override
    public Jugador process(JugadorEntity entity) {
        Jugador domain = JugadorPersistenceMapper.toDomain(entity);
        domain.setNombres(domain.getNombres().toUpperCase());
        domain.setApellidos(domain.getApellidos().toUpperCase());
        return domain;
    }
}
