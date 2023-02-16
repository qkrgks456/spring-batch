package com.example.springbatch.batch.listner;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

@Component
public class TestJobListener {

    @BeforeJob
    public void beforeJob() {
        System.out.println("job 시작 전에");
    }

    @AfterJob
    public void afterJob() {
        System.out.println("job 끝난 후");
    }


}
