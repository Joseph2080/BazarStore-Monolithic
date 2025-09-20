package org.bazar.bazarstore_v2.common.exception;

public class EntityReferenceException extends RuntimeException {

    public EntityReferenceException(String message) {
        super(message);
    }
    public EntityReferenceException(String message, Throwable cause) {
        super(message);
    }
}
