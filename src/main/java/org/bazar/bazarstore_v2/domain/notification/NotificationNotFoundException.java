package org.bazar.bazarstore_v2.domain.notification;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class NotificationNotFoundException extends EntityNotFoundException {
    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
