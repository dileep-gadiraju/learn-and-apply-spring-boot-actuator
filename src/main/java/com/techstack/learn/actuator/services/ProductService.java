package com.techstack.learn.actuator.services;

import com.techstack.learn.actuator.domain.Product;

import java.util.List;

public interface ProductService {

    Product getProduct(Integer id);

    List<Product> listProducts();
}
