package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.NotificationDto;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.NotificationMst;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.NotificationRepository;
import com.findmecore.findmecore.repo.UserRepository;
import com.findmecore.findmecore.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.findmecore.findmecore.entity.Role.ROLE_EMPLOYEE;
import static com.findmecore.findmecore.entity.Role.ROLE_EMPLOYER;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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

    @Override
    public List<NotificationDto> fetchNotification(String userId, String empId) {

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> {
                    throw new RuntimeException("User not found");
                });

        boolean isEmployee = user.getRoles().stream().anyMatch(c -> c.getName().equals(ROLE_EMPLOYEE));

        return notificationRepository.findAllByReceiverIdAndParty(Long.valueOf(empId),
                isEmployee ? ROLE_EMPLOYEE : ROLE_EMPLOYER).stream().
                filter(obj ->  !obj.isRead()).map(this::convertNotEndToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean markNotificationAsRead(String notificationId) {

        NotificationMst notification = findNotificationById(notificationId);

        notification.setRead(true);
        notificationRepository.save(notification);

        return true;
    }

    /**
     * Get the notification by the id string
     * @param notificationId
     * @return
     */
    private NotificationMst findNotificationById(String notificationId) {
        return notificationRepository.findById(Long.valueOf(notificationId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Notification not found");
                });
    }

    private NotificationDto convertNotEndToDto(NotificationMst mst) {
        NotificationDto build = NotificationDto.builder().build();
        build.setTimeAgo(getDays(mst.getTimeAgo()));
        BeanUtils.copyProperties(mst, build);
        return build;
    }

    private Employee findEmployeeById(String empId) {
        return employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Employee not found");
                });
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
