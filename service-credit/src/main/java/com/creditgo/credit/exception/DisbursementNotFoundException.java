package com.creditgo.credit.exception;

public class DisbursementNotFoundException extends RuntimeException {
    public DisbursementNotFoundException(String message) {
        super(message);
    }
}
