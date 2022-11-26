package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.MessageDto;
import com.findmecore.findmecore.dto.NotificationDto;
import com.findmecore.findmecore.entity.NotificationMst;
import com.findmecore.findmecore.repo.NotificationRepository;
import com.findmecore.findmecore.service.MessageService;
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
public class MessageServiceImpl implements MessageService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void createMsg(MessageDto msgDto) {
        rabbitTemplate.convertAndSend("findMe-exchange","chat-route",msgDto);
    }
}
