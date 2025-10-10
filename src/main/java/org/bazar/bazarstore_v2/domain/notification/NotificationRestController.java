package org.bazar.bazarstore_v2.domain.notification;

import org.bazar.bazarstore_v2.domain.notification.factory.NotificationDispatcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationRestController {

    private final NotificationDispatcherFactory notificationDispatcherFactory;

    @Autowired
    public NotificationRestController(NotificationDispatcherFactory notificationDispatcherFactory) {
        this.notificationDispatcherFactory = notificationDispatcherFactory;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody NotificationRequestDto notificationRequestDto) {
        notificationDispatcherFactory.send(notificationRequestDto);
        return buildResponse(null,
                HttpStatus.OK,
                "notification sent successfully.");
    }
}
