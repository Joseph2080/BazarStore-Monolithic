package org.bazar.bazarstore_v2.domain.notification;

public interface NotificationDispatcherIF {
    void sendNotification(NotificationRequestDto notificationRequestDto);
}
