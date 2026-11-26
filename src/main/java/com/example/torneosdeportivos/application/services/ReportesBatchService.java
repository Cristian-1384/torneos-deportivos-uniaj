package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.ReportesBatchUseCase;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportesBatchService implements ReportesBatchUseCase {

    private final JobLauncher jobLauncher;
    private final Job reporteTorneosJob;
    private final Job reporteJugadoresInactivosJob;

    public ReportesBatchService(
            JobLauncher jobLauncher,
            @Qualifier("reporteTorneosJob") Job reporteTorneosJob,
            @Qualifier("reporteJugadoresInactivosJob") Job reporteJugadoresInactivosJob) {
        this.jobLauncher = jobLauncher;
        this.reporteTorneosJob = reporteTorneosJob;
        this.reporteJugadoresInactivosJob = reporteJugadoresInactivosJob;
    }

    @Override
    public void ejecutarReporteTorneos() {
        lanzarJob(reporteTorneosJob);
    }

    @Override
    public void ejecutarReporteJugadoresInactivos() {
        lanzarJob(reporteJugadoresInactivosJob);
    }

    private void lanzarJob(Job job) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("ejecucionId", UUID.randomUUID().toString())
                    .toJobParameters();
            jobLauncher.run(job, params);
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando el proceso Batch: " + e.getMessage());
        }
    }
}
