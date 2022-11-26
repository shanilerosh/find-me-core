package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.NotificationDto;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface NotificationService {

    void createNotification(NotificationDto notificationDto);

    List<NotificationDto> fetchNotification(String userId, String empId);

    Boolean markNotificationAsRead(String notificationId);
}
