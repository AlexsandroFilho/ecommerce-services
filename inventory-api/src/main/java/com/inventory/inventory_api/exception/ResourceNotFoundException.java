package com.inventory.inventory_api.exception;

public class ResourceNotFoundException extends BusinessRuleException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
