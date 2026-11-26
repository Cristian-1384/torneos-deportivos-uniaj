package com.example.torneosdeportivos.infrastructure.adapters.in.cron;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class CronService {

    private final JobLauncher jobLauncher;
    private final Job reporteTorneosJob;
    private final Job reporteJugadoresInactivosJob;
    private final ZoneId zona;

    public CronService(
            JobLauncher jobLauncher,
            @Qualifier("reporteTorneosJob") Job reporteTorneosJob,
            @Qualifier("reporteJugadoresInactivosJob") Job reporteJugadoresInactivosJob,
            @Value("${app.taller.zona:America/Bogota}") String zona) {

        this.jobLauncher = jobLauncher;
        this.reporteTorneosJob = reporteTorneosJob;
        this.reporteJugadoresInactivosJob = reporteJugadoresInactivosJob;
        this.zona = ZoneId.of(zona);
    }

    @Scheduled(cron = "${app.taller.cron:-}", zone = "${app.taller.zona:America/Bogota}")
    public void ejecutarReportes() throws Exception {
        JobParameters parametros = new JobParametersBuilder()
                .addString("fechaCorte", LocalDate.now(zona).toString())
                .addString("ejecucionId", UUID.randomUUID().toString())
                .toJobParameters();

        jobLauncher.run(reporteTorneosJob, parametros);
        jobLauncher.run(reporteJugadoresInactivosJob, parametros);
    }
}
