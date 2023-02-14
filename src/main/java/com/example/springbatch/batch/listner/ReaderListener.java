package com.example.springbatch.batch.listner;

import com.example.springbatch.batch.domain.Test;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
public class ReaderListener implements ItemReadListener<Test> {

    @Override
    public void beforeRead() {
        System.out.println("읽기 전");
    }

    @Override
    public void afterRead(Test item) {
        System.out.println("읽기 후");
    }

    @Override
    public void onReadError(Exception ex) {

    }
}
