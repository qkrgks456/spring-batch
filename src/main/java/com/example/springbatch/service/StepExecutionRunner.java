package com.example.springbatch.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StepExecutionRunner {

    /*private final Job job;
    private final JobLauncher jobLauncher;

    public StepExecutionRunner(@Qualifier("helloJob") Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }*/

    @PostConstruct
    public void init() {

    }
}
