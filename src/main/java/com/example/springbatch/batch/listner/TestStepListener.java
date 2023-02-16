package com.example.springbatch.batch.listner;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class TestStepListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        System.out.println("step 이름 : " + context.getStepContext().getStepName());
        System.out.println("청크 시작전 확인 로그");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        System.out.println("step 이름 : " + context.getStepContext().getStepName());
        System.out.println("청크 끝난후 확인 로그");
    }

}
