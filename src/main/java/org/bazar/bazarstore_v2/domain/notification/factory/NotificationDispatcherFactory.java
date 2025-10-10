package org.bazar.bazarstore_v2.domain.notification.factory;

import org.bazar.bazarstore_v2.domain.notification.NotificationDispatcherIF;
import org.bazar.bazarstore_v2.domain.notification.NotificationRequestDto;
import org.bazar.bazarstore_v2.domain.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationDispatcherFactory {

    private final Map<NotificationType, NotificationDispatcherIF> notificationServiceMap;

    @Autowired
    public NotificationDispatcherFactory(
            Map<NotificationType, NotificationDispatcherIF> notificationServiceMap) {
        this.notificationServiceMap = notificationServiceMap;
    }

    public void send(NotificationRequestDto notificationRequestDto) {
        NotificationType notificationType = notificationRequestDto.getType();
        NotificationDispatcherIF dispatcher = notificationServiceMap.get(notificationType);
        if (dispatcher == null) {
            throw new IllegalArgumentException("Unsupported notification type: " + notificationType);
        }
        dispatcher.sendNotification(notificationRequestDto);
    }
}
