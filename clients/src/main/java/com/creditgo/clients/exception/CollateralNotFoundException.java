package com.creditgo.clients.exception;

public class CollateralNotFoundException extends RuntimeException {
    public CollateralNotFoundException(String message) {
        super(message);
    }
}
