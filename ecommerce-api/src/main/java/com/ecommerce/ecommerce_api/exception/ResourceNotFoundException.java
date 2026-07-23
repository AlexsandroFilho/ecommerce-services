package com.ecommerce.ecommerce_api.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
