package com.example.torneosdeportivos.infrastructure.adapters.in.batch;

import com.example.torneosdeportivos.infrastructure.adapters.out.persistence.repositories.TorneoJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ReporteTorneosTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ReporteTorneosTasklet.class);
    private final TorneoJpaRepository torneoJpaRepository;

    public ReporteTorneosTasklet(TorneoJpaRepository torneoJpaRepository) {
        this.torneoJpaRepository = torneoJpaRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        long totalTorneos = torneoJpaRepository.count();
        long enCurso = torneoJpaRepository.findAll().stream()
                .filter(t -> "EN_CURSO".equalsIgnoreCase(t.getEstado()))
                .count();

        log.info("=== TASKLET REPORTE TORNEOS === Total registrados: {} | Torneos EN_CURSO: {}",
                totalTorneos, enCurso);

        return RepeatStatus.FINISHED;
    }
}
