package com.mmaozi.di.exception;

public class AppStartFailedException extends DiBaseException {

    public AppStartFailedException(String message) {
        super(message);
    }

    public AppStartFailedException(String message, Exception ex) {
        super(message, ex);
    }
}
