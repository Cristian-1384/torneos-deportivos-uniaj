package com.example.torneosdeportivos.infrastructure.adapters.in.batch;

import com.example.torneosdeportivos.domain.models.Jugador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class JugadorInactivoItemWriter implements ItemWriter<Jugador> {

    private static final Logger log = LoggerFactory.getLogger(JugadorInactivoItemWriter.class);

    @Override
    public void write(Chunk<? extends Jugador> chunk) {
        log.info("=== REPORTE BATCH: {} JUGADORES INACTIVOS ENCONTRADOS ===", chunk.size());
        for (Jugador j : chunk) {
            log.info("Jugador Inactivo -> Cédula: {} | Nombre: {} {} | Equipo ID: {}",
                    j.getCedula(), j.getNombres(), j.getApellidos(), j.getIdEquipo());
        }
    }
}
