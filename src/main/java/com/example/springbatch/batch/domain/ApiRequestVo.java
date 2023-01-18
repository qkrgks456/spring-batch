package com.example.springbatch.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiRequestVo {

    private long id;
    private ProductVo productVo;

    @Builder
    public ApiRequestVo(long id, ProductVo productVo) {
        this.id = id;
        this.productVo = productVo;
    }
}
