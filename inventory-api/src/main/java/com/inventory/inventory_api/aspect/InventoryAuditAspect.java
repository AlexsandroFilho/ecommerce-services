package com.inventory.inventory_api.aspect;


import com.inventory.inventory_api.annotation.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class InventoryAuditAspect {

    @Around("@annotation(auditLog)")
    public Object logExecution(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(" [Log info ] Ação: '{}' | Método: {}() | Parâmetros de Entrada: {} ",
                auditLog.action(), methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        Object result;
        try{
            result = joinPoint.proceed();
        } catch(Throwable ex) {
            log.error(" [Log info] Ação: '{}' | Método: {}() | Motivo do Erro: {}",
                    auditLog.action(), methodName, ex.getMessage());

            throw ex;
        }

        long timeTaken = System.currentTimeMillis() - startTime;

        log.info(" [Log info] Ação: '{}' | Tempo de Execução: {}ms | Retorno: {}",
                auditLog.action(), timeTaken, result);

        return result;
    }

}
