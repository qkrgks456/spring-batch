package com.example.springbatch.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResponseVo {

    private String status;
    private String message;


    @Builder
    public ApiResponseVo(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
