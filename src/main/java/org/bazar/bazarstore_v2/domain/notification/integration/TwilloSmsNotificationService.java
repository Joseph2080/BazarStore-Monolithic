package org.bazar.bazarstore_v2.domain.notification.integration;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.bazar.bazarstore_v2.domain.notification.AbstractNotificationService;
import org.bazar.bazarstore_v2.domain.notification.NotificationDispatcherIF;
import org.bazar.bazarstore_v2.domain.notification.NotificationDtoMapper;
import org.bazar.bazarstore_v2.domain.notification.NotificationProcessingException;
import org.bazar.bazarstore_v2.domain.notification.NotificationRepository;
import org.bazar.bazarstore_v2.domain.notification.NotificationRequestDto;
import org.bazar.bazarstore_v2.domain.notification.NotificationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilloSmsNotificationService extends AbstractNotificationService implements NotificationDispatcherIF {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromNumber;

    @Autowired
    public TwilloSmsNotificationService(NotificationRepository repository, NotificationDtoMapper mapper) {
        super(repository, mapper);
    }
    //things I need to are set a global code style as well while writing tests for this application
    @Override
    public void sendNotification(NotificationRequestDto notificationRequestDto) {
        NotificationResponseDto notificationResponseDto = super.create(notificationRequestDto);
        Long notificationId = notificationResponseDto.getNotificationId();

        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new com.twilio.type.PhoneNumber(notificationRequestDto.getRecipient()),
                    new com.twilio.type.PhoneNumber(fromNumber),
                    notificationRequestDto.getMessage()
            ).create();
            // need to check if the message was sent successfully or not as well
            super.updateNotificationStatusSuccess(notificationId);
        } catch (Exception exception) {
            String message = exception.getMessage();
            super.updateNotificationStatusFailed(notificationId, message);
            throw new NotificationProcessingException(message, exception);
        }
    }
}
