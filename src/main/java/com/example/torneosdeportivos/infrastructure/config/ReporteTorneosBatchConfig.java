package com.example.torneosdeportivos.infrastructure.config;

import com.example.torneosdeportivos.infrastructure.adapters.in.batch.ReporteTorneosTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ReporteTorneosBatchConfig {

    @Bean
    public Job reporteTorneosJob(JobRepository jobRepository, Step reporteTorneosStep) {
        return new JobBuilder("reporteTorneosJob", jobRepository)
                .start(reporteTorneosStep)
                .build();
    }

    @Bean
    public Step reporteTorneosStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ReporteTorneosTasklet tasklet) {

        return new StepBuilder("reporteTorneosStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
