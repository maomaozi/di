package com.mmaozi.di.exception;

public class CreateInstanceFailedException extends DiBaseException {

    public CreateInstanceFailedException(String message) {
        super(message);
    }

    public CreateInstanceFailedException(String message, Exception ex) {
        super(message, ex);
    }
}
