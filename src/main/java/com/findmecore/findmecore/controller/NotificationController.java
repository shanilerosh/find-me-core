package com.findmecore.findmecore.controller;

import com.findmecore.findmecore.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShanilErosh
 */
@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    // Initialize Notifications
    private NotificationDto notifications = new NotificationDto(0);

    @GetMapping("/notify")
    public String getNotification() {

        // Increment Notification by one
        notifications.increment();

        // Push notifications to front-end
        template.convertAndSend("/topic/notification", notifications);

        return "Notifications successfully sent to Angular !";
    }
}
