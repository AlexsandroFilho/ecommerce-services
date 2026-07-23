package com.ecommerce.ecommerce_api.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductMessagingConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_CREATE_QUEUE = "inventory.product.create.queue";
    public static final String ROUTING_KEY = "product.created";

    public static final String PRODUCT_DLX = "product.dlx";
    public static final String PRODUCT_CREATE_DLQ = "inventory.product.create.dlq";
    public static final String DLQ_ROUTING_KEY = "product.created.dlq";

    @Bean
    public DirectExchange productExchange(){
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public DirectExchange productDlx(){
        return new DirectExchange(PRODUCT_DLX);
    }

    @Bean
    public Queue productCreateQueue(){
        return QueueBuilder.durable(PRODUCT_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue productCreateDlq(){
        return QueueBuilder.durable(PRODUCT_CREATE_DLQ).build();
    }

    @Bean
    public Binding bindingProductCreate(){
        return BindingBuilder
                .bind(productCreateQueue())
                .to(productExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingProductCreateDlq(){
        return BindingBuilder
                .bind(productCreateDlq())
                .to(productDlx())
                .with(DLQ_ROUTING_KEY);
    }

}
