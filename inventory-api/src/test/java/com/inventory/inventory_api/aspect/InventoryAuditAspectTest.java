package com.inventory.inventory_api.aspect;

import com.inventory.inventory_api.annotation.AuditLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryAuditAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private AuditLog auditLog;

    @Mock
    private Signature signature;

    @InjectMocks
    private InventoryAuditAspect aspect;

    private Object[] args;
    private String expectedResult;

    @BeforeEach
    void setUp() {
        args = new Object[]{"param1", 100};
        expectedResult = "Success";

        lenient().when(joinPoint.getSignature()).thenReturn(signature);
        lenient().when(signature.getName()).thenReturn("testMethod");
        lenient().when(joinPoint.getArgs()).thenReturn(args);
        lenient().when(auditLog.action()).thenReturn("TEST_ACTION");
    }

    @Test
    @DisplayName("Deve executar o aspecto com sucesso quando o metodo prosseguir normalmente")
    void shouldLogAndProceedSuccessfully() throws Throwable {
        when(joinPoint.proceed()).thenReturn(expectedResult);

        Object result = aspect.logExecution(joinPoint, auditLog);

        assertEquals(expectedResult, result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("Deve registrar e relancar a excecao quando o metodo falhar")
    void shouldLogAndRethrowExceptionWhenMethodThrows() throws Throwable {
        RuntimeException exception = new RuntimeException("Erro de teste");
        when(joinPoint.proceed()).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> aspect.logExecution(joinPoint, auditLog));
        verify(joinPoint, times(1)).proceed();
    }
}