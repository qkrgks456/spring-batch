package com.example.springbatch.batch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ShareBean {
    private Map<String, Integer> shareDataMap;


    public ShareBean() {
        this.shareDataMap = new ConcurrentHashMap<>();
        this.shareDataMap.put("count", 0);
    }

    public void putData(String key, Integer data) {
        if (shareDataMap == null) {
            return;
        }

        shareDataMap.put(key, data);
    }

    public Integer getData(String key) {
        if (shareDataMap == null) {
            return null;
        }

        return shareDataMap.get(key);
    }

    public int getSize() {
        if (this.shareDataMap == null) {
            return 0;
        }

        return shareDataMap.size();
    }


}
