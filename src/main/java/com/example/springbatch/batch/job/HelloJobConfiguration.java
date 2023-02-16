package com.example.springbatch.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
        return stepBuilderFactory.get("helloStep87")
                .tasklet((contribution, chunkContext) -> {
                    // 여기에 비즈니스 로직
                    System.out.println("아아 테스트용");
                    return RepeatStatus.FINISHED;
                    // return RepeatStatus.CONTINUABLE;
                })
                .build();
    }

    @Bean
    public Step helloStep99() {
        return stepBuilderFactory.get("helloStep99")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // 여기에 비즈니스 로직
                        System.out.println("아아 테스트용");
                        return RepeatStatus.FINISHED;
                        // return RepeatStatus.CONTINUABLE;
                    }
                })
                .build();
    }



}
