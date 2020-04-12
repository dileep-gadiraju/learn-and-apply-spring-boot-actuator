package com.techstack.learn.actuator.repositories;

import com.techstack.learn.actuator.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
