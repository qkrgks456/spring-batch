package com.example.springbatch.controller;

import com.example.springbatch.batch.domain.ApiResponseVo;
import com.example.springbatch.service.api.ApiService1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ApiService1 apiService1;

    @GetMapping("/api/product/1")
    public ApiResponseVo product1() throws Exception {
        Thread.sleep(11000);
        System.out.println("요청 첫번째");
        return ApiResponseVo.builder()
                .status("200")
                .message("성공")
                .build();
    }

    @GetMapping("/api/product/2")
    public ApiResponseVo product2() throws Exception {
        Thread.sleep(8000);
        System.out.println("요청 두번째");
        return ApiResponseVo.builder()
                .status("200")
                .message("성공")
                .build();
    }


    @GetMapping("/api/product/3")
    public ApiResponseVo product3() {
        System.out.println("요청 세번째");
        return ApiResponseVo.builder()
                .status("200")
                .message("성공")
                .build();
    }

    @GetMapping("/test")
    public String test123() {
        System.out.println("기본요청");
        Mono<ApiResponseVo> test1 = apiService1.test();
        Mono<ApiResponseVo> test2 = apiService1.test2();
        Mono<ApiResponseVo> test3 = apiService1.test3();
        ApiResponseVo t1 = Mono.zip(test2, test1, test3).block()
                .getT1();// 두개 요청 비동기 처리 완료 될때까지
        System.out.println("t1 = " + t1);
        return "ok";
    }


}
