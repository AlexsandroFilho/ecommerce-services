package com.ecommerce.ecommerce_api.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderMessagingConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_RESERVE_QUEUE = "inventory.order.reserve.queue";
    public static final String ROUTING_KEY = "order.created";

    public static final String ORDER_DLX = "order.dlx";
    public static final String ORDER_RESERVE_DLQ = "inventory.order.reserve.dlq";
    public static final String DLQ_ROUTING_KEY = "order.created.dlq";

    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange orderDlx(){
        return new DirectExchange(ORDER_DLX);
    }

    @Bean
    public Queue orderReserveQueue(){
        return QueueBuilder.durable(ORDER_RESERVE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderReserveDlq(){
        return QueueBuilder.durable(ORDER_RESERVE_DLQ).build();
    }

    @Bean
    public Binding bindingOrderCreate(){
        return BindingBuilder
                .bind(orderReserveQueue())
                .to(orderExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderReserveDlq(){
        return  BindingBuilder
                .bind(orderReserveDlq())
                .to(orderDlx())
                .with(DLQ_ROUTING_KEY);
    }

}
