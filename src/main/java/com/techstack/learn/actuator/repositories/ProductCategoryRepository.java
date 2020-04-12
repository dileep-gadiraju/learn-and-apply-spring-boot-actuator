package com.techstack.learn.actuator.repositories;

import com.techstack.learn.actuator.domain.ProductCategory;
import org.springframework.data.repository.CrudRepository;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Integer> {
}
