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
    private EmployeeRepository employeeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactRepository reactRepository;

    @Autowired
    private CommentRepository commentRepository;



    @PostMapping(value = "/")
    public ResponseEntity<Boolean> createPost(
            @RequestParam(required = false) MultipartFile file, @RequestParam("postDto") String postDto) throws JsonProcessingException {

            PostDtoEmpl postDtoEmpl = objectMapper.readValue(postDto, PostDtoEmpl.class);

        Employee employee = findEmployeeById(postDtoEmpl.getEmployeeId());

        String path = null;

        if(null != file) {
            path = saveFile(file);
        }

        Post post = Post.builder().employee(employee).postText(postDtoEmpl.getPostText())
                .createdDateTime(LocalDateTime.now()).isJob(false).postVisibility(PostVisibility.valueOf(postDtoEmpl
                        .getVisibility())).postImageDestination(path).build();

        postRepository.save(post);

        return ResponseEntity.ok(Boolean.TRUE);
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

    private String getReactionList(Set<Reaction> reactionSet,Set<Comment> commetSet) {
        String reactionString = "";

        if(!reactionSet.isEmpty()) {
            int size = reactionSet.size();
            reactionString += Integer.toString(size).concat(" reactions ");
        }
        if(!reactionSet.isEmpty() && !commetSet.isEmpty()){
            reactionString += "& ";
        }

        if(!commetSet.isEmpty()) {
            int size = commetSet.size();
            reactionString += Integer.toString(size).concat(" comments ");
        }

        return reactionString;
    }

    @GetMapping("/{empId}")
    public ResponseEntity<List<PostDto>> fetchPosts(@PathVariable String empId){


        Employee employee = findEmployeeById(empId);

        List<PostDto> posts = postRepository.findAll().stream().sorted(Comparator.comparing(Post::getCreatedDateTime)
                .reversed()).
                map(obj -> {

                    List<ReactDto> likeList = getListOfReaction(obj,ReactionType.LIKE);

                    List<ReactDto> heartList = getListOfReaction(obj, ReactionType.HEART);


                    Optional<Reaction> employeeLike = getEmployeeReactions(employee, obj, ReactionType.LIKE);
                    Optional<Reaction> employeeHeart = getEmployeeReactions(employee, obj, ReactionType.HEART);

                    List<CommentFrontDto> listOfcomments = getListOfComments(obj);

                    return PostDto.builder()
                    .id(obj.getId())
                    .postText(obj.getPostText())
                    .createdUser(obj.getEmployee().getName())
                    .postImageDestination(obj.getPostImageDestination())
                    .timeFrom(getDays(obj.getCreatedDateTime()))
                    .totalReactions(((long) likeList.size()+(long) heartList.size()))
                    .totalLikes((long) likeList.size())
                    .totalHearts((long) heartList.size())
                    .isHearted(employeeHeart.isPresent())
                    .isLiked(employeeLike.isPresent())
                    .userImg(employee.getProfilePicLocation())
                    .likeList(likeList)
                    .heartedList(heartList)
                    .listOfComments(listOfcomments)
                    .totalComments((long) listOfcomments.size())
                    .reactionCommentCount(getReactionList(obj.getReaction(), obj.getComment()))
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(posts);
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

    private List<CommentFrontDto> getListOfComments(Post obj) {
        return obj.getComment().stream().sorted(Comparator.comparing(Comment::getCreatedDateTime).reversed()).map(cmt -> CommentFrontDto.builder().commentText(cmt.getCmtTxt()).timeAgo(getDays(cmt.getCreatedDateTime()))
                .userProfileImg(cmt.getEmployee().getProfilePicLocation()).username(cmt.getEmployee().getName())
                .build()).collect(Collectors.toList());
    }

    private Optional<Reaction> getEmployeeReactions(Employee employee, Post obj, ReactionType reactionType) {
        return obj.getReaction().stream().filter(lke -> lke.getReactionType()
                .equals(reactionType) && lke.getEmployee().equals(employee)).findFirst();
    }

    private List<ReactDto> getListOfReaction(Post obj,ReactionType reactionType) {
        return obj.getReaction().stream().filter(lke -> lke.getReactionType()
                .equals(reactionType)).map(lkeObj -> ReactDto.builder().userProfileImg(lkeObj.getEmployee().getProfilePicLocation())
                .username(lkeObj.getEmployee().getName()).timeAgo(getDays(lkeObj.getCreatedDateTime())).build()).collect(Collectors.toList());
    }


    /**
     * Method to save the file to specific path
     * @param file
     * @return
     */
    private String saveFile(MultipartFile file) {
        try {
            File path = new File(StringUtils.IMAGE_SAVE_LOCATION_POST + file.getOriginalFilename());
            boolean newFile = path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(file.getBytes());
            output.close();
            return StringUtils.FRONT_DESTINATION.concat(Objects.requireNonNull(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    @GetMapping("/react/{empId}/{postId}/{type}")
    public ResponseEntity<Boolean> reactPost(@PathVariable String empId, @PathVariable String postId,
                                              @PathVariable String type){

        Employee employee = findEmployeeById(Long.valueOf(empId));

        Post post = fetchPostById(postId);

        boolean alreadyExist = reactRepository.findByPostAndEmployee(post, employee)
                .isPresent();

        //reaction already exist
        if(alreadyExist) {

            Reaction reaction = reactRepository.findByPostAndEmployee(post, employee).get();

            //same button clicked
            if(ReactionType.valueOf(type).equals(reaction.getReactionType())) {
                //remove record
                reactRepository.delete(reaction);
            }else {
                //update type
                reaction.setReactionType(ReactionType.valueOf(type));
                reaction.setCreatedDateTime(LocalDateTime.now());
                reactRepository.save(reaction);
            }
        }else{
            //create record
            Reaction reaction = Reaction.builder().reactionType(ReactionType.valueOf(type)).post(post)
                    .employee(employee).createdDateTime(LocalDateTime.now()).build();
            reactRepository.save(reaction);
        }

        //send notification

        return ResponseEntity.ok(Boolean.TRUE);
    }


    @PostMapping("/comment/{empId}/{postId}")
    public ResponseEntity<Boolean> commentPost(@PathVariable String empId, @PathVariable String postId,
                                               @RequestBody CommentDto commentDto){

        Employee employee = findEmployeeById(Long.valueOf(empId));

        Post post = fetchPostById(postId);

        Comment comment = Comment.builder().cmtTxt(commentDto.getCommentTxt())
                .employee(employee).post(post).createdDateTime(LocalDateTime.now()).build();

        commentRepository.save(comment);

        //send notification
        return ResponseEntity.ok(Boolean.TRUE);
    }

    private Post fetchPostById(String postId) {
        return postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Post not found");
                });
    }

    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<CommentFrontDto>> getComments(@PathVariable String postId) {
        Post post = fetchPostById(postId);
        return ResponseEntity.ok(getListOfComments(post));
    }




    }
