package com.example.springbatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestService {

    private final Job apiJob;
    private final Job fileJob;
    private final JobLauncher jobLauncher;


    @PostConstruct
    public void test() throws Exception {

    }
}
