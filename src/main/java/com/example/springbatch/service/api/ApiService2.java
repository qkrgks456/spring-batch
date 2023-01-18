package com.example.springbatch.service.api;

import com.example.springbatch.batch.domain.ApiRequestVo;
import com.example.springbatch.batch.domain.ApiResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService2 {


    private final WebClient webClient;

    public ApiResponseVo service(List<? extends ApiRequestVo> apiRequest) {
        String url = "http://localhost:8081/api/product/2";
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
}
