package com.inventory.inventory_api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

class InventoryApiApplicationTests {

    @Test
    @DisplayName("Deve executar o metodo main e instanciar a aplicacao")
    void shouldRunMainMethodAndInstantiateApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = new String[]{};
            InventoryApiApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(InventoryApiApplication.class, args));
        }

        assertDoesNotThrow(InventoryApiApplication::new);
    }
}