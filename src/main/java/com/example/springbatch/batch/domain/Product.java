package com.example.springbatch.batch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    private Long id;

    private String name;

    private int price;

    private String type;

    public Product(ProductVo productVo) {
        this.id = productVo.getId();
        this.name = productVo.getName();
        this.price = productVo.getPrice();
        this.type = productVo.getType();
    }
}
