package com.creditgo.credit.exception;

public class AlreadyGeneratedException extends RuntimeException {
    public AlreadyGeneratedException(String message) {
        super(message);
    }
}
