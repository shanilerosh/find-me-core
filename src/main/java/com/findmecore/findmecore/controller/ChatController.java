package com.findmecore.findmecore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.utility.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactRepository reactRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ChatRepository chatRepository;

    @PostMapping(value = "/")
    public ResponseEntity<Boolean> createChat(@RequestBody ChatDto chatObj){

        Employee senderEmployee = findEmployeeById(chatObj.getSenderId());

        Employee receivedEmployee = findEmployeeById(chatObj.getReceivedId());

        ChatMst chatEntry = ChatMst.builder().chatContent(chatObj.getChatContent())
                .messageTime(LocalDateTime.now())
                .receivingEmployeeId(receivedEmployee)
                .sendingEmployee(senderEmployee).build();

        chatRepository.save(chatEntry);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping(value = "/{loggedId}/{targedId}")
    public ResponseEntity<List<ChatFrontDto>> getChatData(@PathVariable String loggedId,
                                               @PathVariable String targedId){

        //get employee data
        Employee loggedEmp = findEmployeeById(Long.valueOf(loggedId));
        Employee targetEmp = findEmployeeById(Long.valueOf(targedId));


        List<ChatFrontDto> senderToTarget = getChatHistoryWiseVersa(loggedEmp, targetEmp, true);
        List<ChatFrontDto> targetToSend = getChatHistoryWiseVersa(targetEmp,loggedEmp, false);

        //merge two list
        List<ChatFrontDto> listOfChats = Stream.concat(senderToTarget.stream(), targetToSend.stream())
                .sorted(Comparator.comparing(ChatFrontDto::getId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listOfChats);

    }

    @GetMapping(value = "/recent/{loggedId}")
    public ResponseEntity<List<ChatFrontDto>> getRecentContacts(@PathVariable String loggedId){

        //get employee data
        Employee loggedEmp = findEmployeeById(Long.valueOf(loggedId));
        List<ChatFrontDto> chatsByEmployee = getChatsByEmployee(loggedEmp, loggedEmp);
        return ResponseEntity.ok(chatsByEmployee);
    }

    private List<ChatFrontDto> getChatHistoryWiseVersa(Employee loggedEmp, Employee targetEmp,boolean isLoggedToTarget) {
        return chatRepository.findAllBySendingEmployeeAndReceivingEmployeeId(loggedEmp, targetEmp)
                .stream().map(obj -> ChatFrontDto.builder().chatText(obj.getChatContent())
                        .id(obj.getId())
                        .timeAgo(getDays(obj.getMessageTime()))
                        .userImage(obj.getSendingEmployee().getProfilePicLocation())
                        .username(obj.getSendingEmployee().getName())
                        .isLoggedToTarget(isLoggedToTarget)
                        .build()).collect(Collectors.toList());
    }

    private List<ChatFrontDto> getChatsByEmployee(Employee loggedEmp, Employee targetEmp) {
        List<ChatMst> chats = chatRepository.findAllBySendingEmployeeOrReceivingEmployeeId(loggedEmp, targetEmp);

        List<ChatFrontDto> chatsWithDuplicated = chats.stream().sorted(Comparator.comparing(ChatMst::getMessageTime)
                .reversed()).map(obj -> {
            if (obj.getReceivingEmployeeId().equals(loggedEmp)) {
                return ChatFrontDto.builder().chatText(obj.getChatContent()).username(obj.getSendingEmployee().getName())
                        .id(obj.getSendingEmployee().getEmployeeId())
                        .timeAgo(getDays(obj.getMessageTime()))
                        .userImage(obj.getSendingEmployee().getProfilePicLocation()).build();
            } else {
                return ChatFrontDto.builder().chatText(obj.getChatContent()).username(obj.getReceivingEmployeeId().getName())
                        .id(obj.getReceivingEmployeeId().getEmployeeId())
                        .timeAgo(getDays(obj.getMessageTime()))
                        .userImage(obj.getReceivingEmployeeId().getProfilePicLocation()).build();
            }
        }).collect(Collectors.toList());

        //remove duplicates
        Set<Long> set = new HashSet<>(chatsWithDuplicated.size());
        List<ChatFrontDto> collect = chatsWithDuplicated.stream()
                .filter(p -> set.add(p.getId())).collect(Collectors.toList());

        collect.stream().findFirst().ifPresent(obj -> {
            obj.setRecent(true);
        });

        return collect;

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
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


    private Employee findEmployeeById(String empId) {
        return employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Employee not found");
                });
    }

    private Employee findEmployeeById(Long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> {
                    throw new ResolutionException("Employee not found");
                });
    }




    }
