package com.example.springbatch.batch.listner;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import javax.batch.api.chunk.listener.ChunkListener;
import java.time.LocalTime;

@Component
public class TestListener implements StepExecutionListener {

    private long time;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("스텝 시작");
        long start = LocalTime.now().toNanoOfDay();
        System.out.println(start);
        time = start;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("끝난시간 : " + (LocalTime.now().toNanoOfDay() - time));
        return ExitStatus.COMPLETED;
    }
}
