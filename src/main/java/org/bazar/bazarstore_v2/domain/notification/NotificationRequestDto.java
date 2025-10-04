package org.bazar.bazarstore_v2.domain.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationRequestDto {
    private String recipient;
    private String message;
    private String subject;
    private NotificationType type;

}
