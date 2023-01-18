package com.example.springbatch.batch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ApiInfo {

    private String url;
    private List<? extends ApiRequestVo> apiRequestVos;

}
