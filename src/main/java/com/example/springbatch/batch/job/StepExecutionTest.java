package com.example.springbatch.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration*/
@RequiredArgsConstructor
public class StepExecutionTest {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*@Bean*/
   /* @Qualifier("helloJob")*/
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    /*@Bean*/
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    StepExecution stepExecution = contribution.getStepExecution();
                    System.out.println("step1 executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /*@Bean*/
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /*@Bean*/
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3 executed");
                    return RepeatStatus.FINISHED;
                }).build();

    }
}
