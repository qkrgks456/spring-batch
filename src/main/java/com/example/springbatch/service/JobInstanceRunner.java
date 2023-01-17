package com.example.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class JobInstanceRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    public void init() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user3")
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
