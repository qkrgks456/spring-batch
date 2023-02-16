package com.example.springbatch.batch.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TestService {

    private final JobLauncher jobLauncher;
    private final Job testJob2;

    @Scheduled(cron = "30 * * * * *", zone = "Asia/Seoul")
    public void scheduledTest() throws Exception {
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", current.format(df))
                .toJobParameters();
        jobLauncher.run(testJob2, jobParameters);
    }

}
