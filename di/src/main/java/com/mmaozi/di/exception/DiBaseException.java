package com.mmaozi.di.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiBaseException extends RuntimeException {

    private final String message;

    public DiBaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public DiBaseException(String message) {
        super(message);
        this.message = message;
    }
}
