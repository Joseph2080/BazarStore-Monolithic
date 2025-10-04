package org.bazar.bazarstore_v2.domain.notification;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationDtoMapper implements DtoMapper<Notification,
        NotificationRequestDto,
        NotificationResponseDto > {
    @Override
    public Notification convertDtoToEntity(NotificationRequestDto dto) {
        return Notification.builder()
                .type(dto.getType())
                .recipient(dto.getRecipient())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public NotificationResponseDto convertEntityToResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getId())
                .type(notification.getType())
                .recipient(notification.getRecipient())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .build();
    }

    @Override
    public void updateEntityFromDto(NotificationRequestDto dto, Notification entity) {
        entity.setType(dto.getType());
        entity.setRecipient(dto.getRecipient());
        entity.setSubject(dto.getSubject());
        entity.setMessage(dto.getMessage());
    }
}
