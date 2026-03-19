package com.example.productcatalog.messaging;

import com.example.productcatalog.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private static final String EXCHANGE = "product-events";
    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(Product product) {
        String message = "CREATED:" + product.getId() + ":" + product.getName();
        rabbitTemplate.convertAndSend(EXCHANGE, message);
    }

    public void publishDeleted(Long id) {
        String message = "DELETED:" + id;
        rabbitTemplate.convertAndSend(EXCHANGE, message);
    }
}