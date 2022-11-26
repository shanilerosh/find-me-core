package com.findmecore.findmecore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.findmecore.findmecore.dto.EmployeeDto;
import com.findmecore.findmecore.dto.NotificationDto;
import com.findmecore.findmecore.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/{userId}/{empId}")
    public ResponseEntity<List<NotificationDto>> fetchNotificationDto(@PathVariable String userId,@PathVariable String empId) {

        return ResponseEntity.ok(notificationService.fetchNotification(userId, empId));

    }

    @GetMapping("/read/{notificationId}")
    public ResponseEntity<Boolean> markNotificationAsRead(@PathVariable String notificationId) {

        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));

    }



}
