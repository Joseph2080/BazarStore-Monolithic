package org.bazar.bazarstore_v2.domain.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class NotificationResponseDto {
    private Long notificationId;
    private String subject;
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
    private NotificationType type;
    private String recipient;
    private String message;
}
