package org.bazar.bazarstore_v2.domain.notification;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;

import java.time.LocalDateTime;

public class AbstractNotificationService extends AbstractJpaService<Notification,
        Long,
        NotificationRequestDto,
        NotificationResponseDto,
        NotificationRepository
        > implements NotificationService{

    private final static String NOTIFICATION_NOT_FOUND = "Notification not found";
    public AbstractNotificationService(NotificationRepository repository, NotificationDtoMapper mapper) {
        super(repository, mapper);
    }

    public void updateNotificationStatusSuccess(Long notificationId) {
        Notification notification = findEntityByIdOrElseThrowException(notificationId);
        notification.setStatus(NotificationStatus.SENT);;
        notification.setSentAt(LocalDateTime.now());
        repository.save(notification);
    }

    public void updateNotificationStatusFailed(Long notificationId, String errorMessage) {
        Notification notification = findEntityByIdOrElseThrowException(notificationId);
        notification.setSentAt(LocalDateTime.now());
        notification.setStatus(NotificationStatus.FAILED);
        notification.setErrorMessage(errorMessage);
        repository.save(notification);
    }
    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new NotificationNotFoundException(NOTIFICATION_NOT_FOUND);
    }
}
