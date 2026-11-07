package com.creditgo.credit.exception;

public class CollateralNotFoundException extends RuntimeException {
    public CollateralNotFoundException(String message) {
        super(message);
    }
}
