package com.findmecore.findmecore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Comment;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Post;
import com.findmecore.findmecore.entity.Reaction;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.repo.CommentRepository;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.PostRepository;
import com.findmecore.findmecore.repo.ReactRepository;
import com.findmecore.findmecore.service.PostService;
import com.findmecore.findmecore.utility.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;


    @PostMapping(value = "/")
    public ResponseEntity<Boolean> createPost(
            @RequestParam(required = false) MultipartFile file, @RequestParam("postDto") String postDto) throws JsonProcessingException {

            PostDtoEmpl postDtoEmpl = objectMapper.readValue(postDto, PostDtoEmpl.class);

        return ResponseEntity.ok(postService.createPost(file, postDtoEmpl));

    }


    @GetMapping("/{empId}")
    public ResponseEntity<List<PostDto>> fetchPosts(@PathVariable String empId){
        return ResponseEntity.ok(postService.fetchPosts(empId));
    }

    @GetMapping("/friend/{empId}")
    public ResponseEntity<List<PostDto>> fetchFriendsPosts(@PathVariable String empId){
        return ResponseEntity.ok(postService.fetchFriendsPosts(empId));
    }

    @GetMapping("/react/{empId}/{postId}/{type}")
    public ResponseEntity<Boolean> reactPost(@PathVariable String empId, @PathVariable String postId,
                                              @PathVariable String type){

        return ResponseEntity.ok(postService.reactPost(empId, postId, type));
    }


    @PostMapping("/comment/{empId}/{postId}")
    public ResponseEntity<Boolean> commentPost(@PathVariable String empId, @PathVariable String postId,
                                               @RequestBody CommentDto commentDto){

        return ResponseEntity.ok(postService.commentPost(empId,postId,commentDto));
    }


    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<CommentFrontDto>> getComments(@PathVariable String postId){
        return ResponseEntity.ok(postService.getComments(postId));
    }

    }
