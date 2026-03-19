package com.example.productcatalog.messaging;

import com.example.productcatalog.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private static final String EXCHANGE = "product-events";
    private final RabbitTemplate rabbitTemplate;
    private final StreamBridge streamBridge;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate, StreamBridge streamBridge) {
        this.rabbitTemplate = rabbitTemplate;
        this.streamBridge = streamBridge;
    }

    public void publishCreated(Product product) {
        String message = "CREATED:" + product.getId() + ":" + product.getName();
        rabbitTemplate.convertAndSend(EXCHANGE, message);
        // Alternatively, using StreamBridge:
        // streamBridge.send("productEvents-out-0", message);
    }

    public void publishDeleted(Long id) {
        String message = "DELETED:" + id;
        rabbitTemplate.convertAndSend(EXCHANGE, message);
        // Alternatively, using StreamBridge:
        // streamBridge.send("productEvents-out-0", message);
    }
}