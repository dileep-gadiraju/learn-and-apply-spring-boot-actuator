package com.techstack.learn.actuator.services;

import com.techstack.learn.actuator.domain.Product;
import com.techstack.learn.actuator.repositories.ProductRepository;
import com.techstack.learn.actuator.services.jms.JmsTextMessageService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Karthikeyan Nithiyanandam
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private JmsTextMessageService jmsTextMessageService;
    private Counter productCounter;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, JmsTextMessageService jmsTextMessageService,
                              MeterRegistry registry) {
        this.productRepository = productRepository;
        this.jmsTextMessageService = jmsTextMessageService;
        this.productCounter = registry.counter("com.techstack.learn.actuator.services.getproduct");
    }

    @Override
    public Product getProduct(Integer id) {
        jmsTextMessageService.sendTextMessage("Fetching Product ID: " + id );
        productCounter.increment();
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> listProducts() {
        jmsTextMessageService.sendTextMessage("Listing Products");
        return IteratorUtils.toList(productRepository.findAll().iterator());
    }

}
