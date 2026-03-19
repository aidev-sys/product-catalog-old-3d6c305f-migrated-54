package com.example.productcatalog.service;

import com.example.productcatalog.messaging.ProductEventPublisher;
import com.example.productcatalog.model.Product;
import com.example.productcatalog.model.ProductNotFoundException;
import com.example.productcatalog.repository.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductEventPublisher eventPublisher;
    private final StreamBridge streamBridge;

    public ProductService(ProductRepository repository, ProductEventPublisher eventPublisher, StreamBridge streamBridge) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.streamBridge = streamBridge;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        Product saved = repository.save(product);
        streamBridge.send("product-created-out-0", saved);
        return saved;
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        repository.deleteById(id);
        streamBridge.send("product-deleted-out-0", id);
    }
}