package com.example.springbatch.repository;

import com.example.springbatch.batch.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.type from Product p group by p.type")
    List<String> findTypeName();

}
