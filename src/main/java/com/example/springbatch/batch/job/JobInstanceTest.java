package com.example.springbatch.batch.job;

import com.example.springbatch.batch.listner.TestListener;
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
public class JobInstanceTest {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TestListener testListener;

    // job,step,flow,tasklet은 실제 진행되는 흐름
    // jobInstance,JobParameter,JobExecution,StepExecution 등등 위 흐름진행중 메타데이터 적재를 위해 쓰는 도메인들
    // JobInstance -> 매일 실행되는 각각의 Job

    @Bean
    public Job helloJob3() {
        return jobBuilderFactory.get("helloJob3")
                .start(helloStep1())
                .next(helloStep2())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((contribution, chunkContext) -> {
                    contribution.getExitStatus();
                    System.out.println("여기 비즈니스 로직");
                    return RepeatStatus.FINISHED;
                })
                .startLimit(10)
                .allowStartIfComplete(true)
                .listener(testListener)
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
