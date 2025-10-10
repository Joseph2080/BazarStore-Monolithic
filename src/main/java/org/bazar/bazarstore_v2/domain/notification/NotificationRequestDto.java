package org.bazar.bazarstore_v2.domain.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationRequestDto {

    @NotBlank(message = "Recipient cannot be blank")
    @Size(max = 255, message = "Recipient must not exceed 255 characters")
    private String recipient;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    @Size(max = 255, message = "Subject must not exceed 255 characters")
    private String subject;

    @NotNull(message = "Notification type is required (EMAIL or SMS)")
    private NotificationType type;

}
