package com.example.springbatch.batch.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResponseVo {

    private String status;
    private String message;


    @Builder
    public ApiResponseVo(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
