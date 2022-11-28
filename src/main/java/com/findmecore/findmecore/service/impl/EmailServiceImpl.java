package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.EmailDto;
import com.findmecore.findmecore.dto.NotificationDto;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.NotificationMst;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.NotificationRepository;
import com.findmecore.findmecore.repo.UserRepository;
import com.findmecore.findmecore.service.EmailService;
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
public class EmailServiceImpl implements EmailService
{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void createEmail(EmailDto emailDto) {
        //push to the notification que
        rabbitTemplate.convertAndSend("findMe-exchange","email-route",emailDto);
    }
}
