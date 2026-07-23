package com.ecommerce.ecommerce_api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RabbitMqConfigTest {

    private RabbitMqConfig config;
    private ConnectionFactory connectionFactory;
    private RabbitAdmin rabbitAdmin;
    private ApplicationReadyEvent applicationReadyEvent;

    @BeforeEach
    void setUp() {
        config = new RabbitMqConfig();
        connectionFactory = mock(ConnectionFactory.class);
        rabbitAdmin = mock(RabbitAdmin.class);
        applicationReadyEvent = mock(ApplicationReadyEvent.class);
    }

    @Test
    @DisplayName("Deve criar o bean RabbitAdmin com sucesso")
    void shouldCreateRabbitAdmin() {
        RabbitAdmin admin = config.rabbitAdmin(connectionFactory);
        assertNotNull(admin);
    }

    @Test
    @DisplayName("Deve inicializar o RabbitAdmin no evento ApplicationReadyEvent")
    void shouldInitializeRabbitAdminOnEvent() {
        ApplicationListener<ApplicationReadyEvent> listener = config.initialize(rabbitAdmin);
        listener.onApplicationEvent(applicationReadyEvent);

        verify(rabbitAdmin, times(1)).initialize();
    }

    @Test
    @DisplayName("Deve criar o bean JacksonJsonMessageConverter com sucesso")
    void shouldCreateJacksonJsonMessageConverter() {
        JacksonJsonMessageConverter converter = config.messageConverter();
        assertNotNull(converter);
    }
}