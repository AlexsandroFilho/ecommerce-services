package com.ecommerce.ecommerce_api.producer;

import com.ecommerce.ecommerce_api.event.OrderCreatedEvent;
import com.ecommerce.ecommerce_api.event.ProductCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private final RabbitTemplate rabbit;

    public NotificationProducer(RabbitTemplate rabbit){
        this.rabbit = rabbit;
    }

    public void sendCreateProduct(Long productId){
        ProductCreatedEvent event = new ProductCreatedEvent(productId);

        rabbit.convertAndSend("product.exchange", "product.created", event);
    }

    public void sendCreateOrder(Long productId, Integer quantity){
        OrderCreatedEvent event = new OrderCreatedEvent(productId, quantity);

        rabbit.convertAndSend("order.exchange", "order.created", event);
    }

}
