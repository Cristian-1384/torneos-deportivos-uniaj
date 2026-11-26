package com.example.torneosdeportivos.infrastructure.config;

import com.example.torneosdeportivos.domain.models.Jugador;
import com.example.torneosdeportivos.infrastructure.adapters.in.batch.JugadorInactivoItemProcessor;
import com.example.torneosdeportivos.infrastructure.adapters.in.batch.JugadorInactivoItemReader;
import com.example.torneosdeportivos.infrastructure.adapters.in.batch.JugadorInactivoItemWriter;
import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.entities.JugadorEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ReporteJugadoresInactivosBatchConfig {

    private static final int CHUNK_SIZE = 50;

    @Bean
    public Job reporteJugadoresInactivosJob(JobRepository jobRepository, Step reporteJugadoresInactivosStep) {
        return new JobBuilder("reporteJugadoresInactivosJob", jobRepository)
                .start(reporteJugadoresInactivosStep)
                .build();
    }

    @Bean
    public Step reporteJugadoresInactivosStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JugadorInactivoItemReader reader,
            JugadorInactivoItemProcessor processor,
            JugadorInactivoItemWriter writer) {

        return new StepBuilder("reporteJugadoresInactivosStep", jobRepository)
                .<JugadorEntity, Jugador>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
