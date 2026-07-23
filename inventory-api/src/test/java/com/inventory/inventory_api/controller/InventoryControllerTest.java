package com.inventory.inventory_api.controller;

import tools.jackson.databind.ObjectMapper;
import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.exception.GlobalExceptionHandler;
import com.inventory.inventory_api.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@Import(GlobalExceptionHandler.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventoryService service;

    private InventoryRequestDto supplyRequest;
    private InventoryResponseDto supplyResponse;
    private InventoryResponseDto reserveResponse;
    private InventoryResponseDto getStockResponse;

    @BeforeEach
    void setUp() {
        supplyRequest = new InventoryRequestDto(1L, 10);
        supplyResponse = new InventoryResponseDto(100L, 1L, 10, true, LocalDateTime.now());
        reserveResponse = new InventoryResponseDto(100L, 1L, 5, true, LocalDateTime.now());
        getStockResponse = new InventoryResponseDto(100L, 1L, 10, true, LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /inventory/supply - Deve abastecer o estoque e retornar HTTP 200 OK")
    void shouldSupplyStockAndReturn200() throws Exception {
        when(service.supplyStock(any(InventoryRequestDto.class))).thenReturn(supplyResponse);

        mockMvc.perform(post("/inventory/supply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(service).supplyStock(any(InventoryRequestDto.class));
    }

    @Test
    @DisplayName("POST /inventory/reserve - Deve reservar o estoque e retornar HTTP 200 OK")
    void shouldReserveStockAndReturn200() throws Exception {
        when(service.reserveStock(eq(1L), eq(5))).thenReturn(reserveResponse);

        mockMvc.perform(post("/inventory/reserve")
                        .param("productId", "1")
                        .param("quantityToReserve", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.quantity").value(5));

        verify(service).reserveStock(eq(1L), eq(5));
    }

    @Test
    @DisplayName("GET /inventory/{productId} - Deve buscar o estoque e retornar HTTP 200 OK")
    void shouldGetStockAndReturn200() throws Exception {
        when(service.getStock(eq(1L))).thenReturn(getStockResponse);

        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(service).getStock(eq(1L));
    }
}