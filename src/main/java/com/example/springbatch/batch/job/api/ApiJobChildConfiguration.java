package com.example.springbatch.batch.job.api;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiJobChildConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;

    @Bean
    @Qualifier("jobStep")
    public Step jobStep(@Qualifier("childJob") Job childJob) {
        return stepBuilderFactory.get("jobStep")
                .job(childJob)
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    @Qualifier("childJob")
    public Job childJob(@Qualifier("apiMasterStep") Step apiMasterStep) {
        return jobBuilderFactory.get("childJob")
                .start(apiMasterStep)
                .build();
    }

}
