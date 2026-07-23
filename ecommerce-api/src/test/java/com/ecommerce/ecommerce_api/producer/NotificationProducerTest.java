package com.ecommerce.ecommerce_api.producer;

import com.ecommerce.ecommerce_api.event.OrderCreatedEvent;
import com.ecommerce.ecommerce_api.event.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    @Mock
    private RabbitTemplate rabbit;

    private NotificationProducer producer;

    private final Long productId = 1L;
    private final Integer quantity = 2;

    @BeforeEach
    void setUp() {
        producer = new NotificationProducer(rabbit);
    }

    @Test
    @DisplayName("Deve enviar evento de criacao de produto com sucesso")
    void shouldSendCreateProductEvent() {
        producer.sendCreateProduct(productId);

        verify(rabbit).convertAndSend(eq("product.exchange"), eq("product.created"), any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve enviar evento de criacao de pedido com sucesso")
    void shouldSendCreateOrderEvent() {
        producer.sendCreateOrder(productId, quantity);

        verify(rabbit).convertAndSend(eq("order.exchange"), eq("order.created"), any(OrderCreatedEvent.class));
    }
}