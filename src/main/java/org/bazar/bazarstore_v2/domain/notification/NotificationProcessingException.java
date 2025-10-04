package org.bazar.bazarstore_v2.domain.notification;

public class NotificationProcessingException extends RuntimeException{
    public NotificationProcessingException(String message) {
        super(message);
    }
    public NotificationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
