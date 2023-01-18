package com.example.springbatch.batch.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductVo {

    private Long id;
    private String name;
    private int price;
    private String type;

    public ProductVo(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.type = product.getType();
    }

    public ProductVo(String type) {
        this.type = type;
    }
}
