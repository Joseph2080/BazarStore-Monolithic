package org.bazar.bazarstore_v2.domain.notification.integration;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
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

import java.io.IOException;

@Service
public class SendGridEmailNotificationService extends AbstractNotificationService implements NotificationDispatcherIF {
    @Value("${sendGrid.apiKey}")
    private String apiKey;
    @Value("${sendGrid.sendEmail}")
    private String senderEmail;

    @Autowired
    public SendGridEmailNotificationService(NotificationRepository notificationRepository, NotificationDtoMapper mapper) {
        super(notificationRepository, mapper);
    }

    @Override
    public void sendNotification(NotificationRequestDto notificationRequestDto) {
        NotificationResponseDto notificationResponseDto = super.create(notificationRequestDto);
        Long notificationId = notificationResponseDto.getNotificationId();
        try {
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();

            Mail mail = new Mail(
                    new Email(senderEmail),
                    notificationResponseDto.getSubject(),
                    new Email(notificationResponseDto.getRecipient()),
                    new Content("text/plain", notificationResponseDto.getMessage())
            );
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() == 202) {
                super.updateNotificationStatusSuccess(notificationId);
            } else {
                String errorMessage = "SendGrid response: " + response.getStatusCode() + " " + response.getBody();
                super.updateNotificationStatusFailed(notificationId, errorMessage);
                throw new NotificationProcessingException(errorMessage);
            }
        } catch (IOException ioException) {
            String message = ioException.getMessage();
            super.updateNotificationStatusFailed(notificationId, message);
            throw new NotificationProcessingException(message, ioException);
        }
    }
}
