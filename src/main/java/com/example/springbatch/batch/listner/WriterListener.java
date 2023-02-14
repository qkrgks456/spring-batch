package com.example.springbatch.batch.listner;

import com.example.springbatch.batch.domain.Test;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriterListener {

    @BeforeWrite
    public void beforeWrite(List<? extends Test> items) {
        System.out.println("시작 리스트 : " + items);
    }

    @AfterWrite
    public void afterWrite(List<? extends Test> items) {
        System.out.println("완료 리스트 : " + items);
    }

}
