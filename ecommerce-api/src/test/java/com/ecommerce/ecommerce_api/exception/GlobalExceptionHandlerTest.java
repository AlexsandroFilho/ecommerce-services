package com.ecommerce.ecommerce_api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private ResourceNotFoundException notFoundException;
    private MethodArgumentNotValidException validationException;
    private BindingResult bindingResult;
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        notFoundException = new ResourceNotFoundException("Recurso nao encontrado");

        bindingResult = mock(BindingResult.class);
        fieldError = new FieldError("object", "name", "Nome e obrigatorio");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        validationException = new MethodArgumentNotValidException(null, bindingResult);
    }

    @Test
    @DisplayName("Deve tratar ResourceNotFoundException e retornar HTTP 404")
    void shouldHandleResourceNotFoundException() {
        ResponseEntity<GlobalExceptionHandler.StandardError> response = handler.handleResourceNotFound(notFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Recurso Não encontrado", response.getBody().error());
        assertEquals("Recurso nao encontrado", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException e retornar HTTP 400 com mapa de erros")
    void shouldHandleValidationErrors() {
        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nome e obrigatorio", response.getBody().get("name"));
    }
}