package com.example.springbatch.batch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "test")
@Getter
public class Test {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "age")
    private int age;

}
