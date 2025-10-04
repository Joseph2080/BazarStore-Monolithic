package org.bazar.bazarstore_v2.domain.notification.factory;

import org.bazar.bazarstore_v2.domain.notification.NotificationDispatcherIF;
import org.bazar.bazarstore_v2.domain.notification.NotificationType;
import org.bazar.bazarstore_v2.domain.notification.integration.SendGridEmailNotificationService;
import org.bazar.bazarstore_v2.domain.notification.integration.TwilloSmsNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class NotificationMapConfig {

    @Bean
    public Map<NotificationType, NotificationDispatcherIF> notificationServiceMap(
            SendGridEmailNotificationService emailService,
            TwilloSmsNotificationService smsService) {
        return Map.of(
                NotificationType.EMAIL, emailService,
                NotificationType.SMS, smsService
        );
    }
}
