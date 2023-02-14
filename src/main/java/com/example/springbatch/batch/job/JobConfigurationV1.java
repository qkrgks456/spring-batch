package com.example.springbatch.batch.job;

import com.example.springbatch.batch.domain.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobConfigurationV1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job helloJob2() {
        return jobBuilderFactory.get("hellojob2")
                .start(step5(null)) // 컴파일 오류나지 않도록 null 설정
                .next(step6())
                .build();
    }

    @Bean
    @JobScope
    public Step step5(@Value("#{jobParameters['requestDate']}") String requestDate) {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("requestDate = " + requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
