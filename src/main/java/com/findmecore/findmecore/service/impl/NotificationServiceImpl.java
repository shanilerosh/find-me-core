package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.NotificationDto;
import com.findmecore.findmecore.entity.NotificationMst;
import com.findmecore.findmecore.repo.NotificationRepository;
import com.findmecore.findmecore.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author ShanilErosh
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void createNotification(NotificationDto notificationDto) {

        //save to the db
        NotificationMst notification = NotificationMst.builder().build();
        BeanUtils.copyProperties(notificationDto, notification,"notificationId");

        notification.setTimeAgo(LocalDateTime.now());

        NotificationMst savedNotification = notificationRepository.save(notification);
        notificationDto.setNotificationId(savedNotification.getNotificationId());

        //push to the notification que
        rabbitTemplate.convertAndSend("findMe-exchange","notification-route",notificationDto);
    }


    private String getDays(LocalDateTime created) {
        long seconds = ChronoUnit.SECONDS.between(created, LocalDateTime.now());
        if(seconds < 60) {
            return seconds+" seconds ago";
        }
        long mins = ChronoUnit.MINUTES.between(created, LocalDateTime.now());

        if(mins < 60) {
            return mins+" minutes ago";
        }
        long hours = ChronoUnit.HOURS.between(created, LocalDateTime.now());
        if(hours < 24) {
            return hours+" minutes ago";
        }

        long days = ChronoUnit.DAYS.between(created, LocalDateTime.now());

        return days+" days ago";
    }
}
