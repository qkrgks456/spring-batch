package com.example.springbatch.service.api;

import com.example.springbatch.batch.domain.ApiRequestVo;
import com.example.springbatch.batch.domain.ApiResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService1 {

    private final WebClient webClient;

    public ApiResponseVo service(List<? extends ApiRequestVo> apiRequest) {
        String url = "http://localhost:8080/api/product/1";
        return webClient.post()
                .uri(url)
                .body(Mono.just(apiRequest), ApiRequestVo.class)
                .retrieve()
                .bodyToMono(ApiResponseVo.class)
                .onErrorReturn(ApiResponseVo.builder()
                        .status("500")
                        .message("에러")
                        .build())
                .block();
    }

    public Mono<ApiResponseVo> test() {
        String url = "http://localhost:8080/api/product/1";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ApiResponseVo.class)
                .subscribeOn(Schedulers.boundedElastic())
                .map(apiResponseVo -> {
                    System.out.println(Thread.currentThread().getName());
                    return apiResponseVo;
                });
    }

    public Mono<ApiResponseVo> test2() {
        String url = "http://localhost:8080/api/product/2";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ApiResponseVo.class)
                .subscribeOn(Schedulers.boundedElastic())
                .map(apiResponseVo -> {
                    System.out.println(Thread.currentThread().getName());
                    return apiResponseVo;
                });
    }

    public Mono<ApiResponseVo> test3() {
        String url = "http://localhost:8080/api/product/3";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ApiResponseVo.class)
                .subscribeOn(Schedulers.boundedElastic())
                .map(apiResponseVo -> {
                    System.out.println(Thread.currentThread().getName());
                    return apiResponseVo;
                });
    }

}
