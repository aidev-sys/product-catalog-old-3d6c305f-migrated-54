package com.example.productcatalog.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class ProductEventSseController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public String subscribe() {
        rabbitTemplate.convertAndSend("product.events", "Event subscribed");
        return "Event subscribed";
    }

    public void broadcast(String message) {
        rabbitTemplate.convertAndSend("product.events", message);
    }
}