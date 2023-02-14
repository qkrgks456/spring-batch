package com.example.springbatch.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob123() {
        return jobBuilderFactory.get("helloJob123")
                .start(helloStep87())
                .build();
    }

    @Bean
    public Step helloStep87() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                    // return RepeatStatus.CONTINUABLE;
                }).build();
    }



}
