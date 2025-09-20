package org.bazar.bazarstore_v2.common.exception;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
    public InvalidFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
