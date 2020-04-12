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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Karthikeyan Nithiyanandam
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private JmsTextMessageService jmsTextMessageService;
    private Counter productCounterForGetProduct;
    private Counter productCounterForListOfProducts;
    private AtomicInteger gauge;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, JmsTextMessageService jmsTextMessageService,
                              MeterRegistry registry) {
        this.productRepository = productRepository;
        this.jmsTextMessageService = jmsTextMessageService;
        this.productCounterForGetProduct =
                registry.counter("com.techstack.learn.actuator.services.ProductService.getproduct");
        this.productCounterForListOfProducts =
                registry.counter("com.techstack.learn.actuator.services.ProductService.listProducts");
        this.gauge =
                registry.gauge("com.techstack.learn.actuator.services.ProductService.pageViewPerMin",
                        new AtomicInteger(0));
    }

    @Override
    public Product getProduct(Integer id) {
        jmsTextMessageService.sendTextMessage("Fetching Product ID: " + id );
        productCounterForGetProduct.increment();
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> listProducts() {
        jmsTextMessageService.sendTextMessage("Listing Products");
        productCounterForListOfProducts.increment();
        gauge.set(25); //==> Hardcoded value. Actual logic will be depends on business use case.
        return IteratorUtils.toList(productRepository.findAll().iterator());
    }

}
