package com.inventory.inventory_api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RabbitMqConfigTest {

    private RabbitMqConfig config;

    @BeforeEach
    void setUp() {
        config = new RabbitMqConfig();
    }

    @Test
    @DisplayName("Deve criar o bean JacksonJsonMessageConverter com sucesso")
    void shouldCreateJacksonJsonMessageConverter() {
        JacksonJsonMessageConverter converter = config.messageConverter();
        assertNotNull(converter);
    }
}