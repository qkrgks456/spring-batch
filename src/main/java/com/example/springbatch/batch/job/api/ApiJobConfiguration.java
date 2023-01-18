package com.example.springbatch.batch.job.api;

import com.example.springbatch.batch.listner.ApiJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.batch.api.listener.JobListener;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job apiJob(@Qualifier("jobStep") Step jobStep) {
        return jobBuilderFactory.get("apiJob")
                .listener(new ApiJobListener())
                .start(apiStep1())
                .next(jobStep)
                .next(apiStep2())
                .build();
    }

    @Bean
    public Step apiStep1() {
        return stepBuilderFactory.get("apiStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> api service is start");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step apiStep2() {
        return stepBuilderFactory.get("apiStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> api service is end");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
