package com.example.torneosdeportivos.infrastructure.adapters.in.batch;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.JugadorJpaRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class JugadorInactivoItemReader implements ItemReader<JugadorEntity> {

    private final JugadorJpaRepository jugadorJpaRepository;
    private Iterator<JugadorEntity> iterator;

    public JugadorInactivoItemReader(JugadorJpaRepository jugadorJpaRepository) {
        this.jugadorJpaRepository = jugadorJpaRepository;
    }

    @Override
    public JugadorEntity read() {
        if (iterator == null) {
            List<JugadorEntity> inactivos = jugadorJpaRepository.findAll().stream()
                    .filter(j -> "INACTIVO".equalsIgnoreCase(j.getEstado()))
                    .toList();
            iterator = inactivos.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
