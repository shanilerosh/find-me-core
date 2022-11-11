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
import java.util.List;
import java.util.Optional;
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



//    let formData = new FormData();
//                formData.append("file", state.fileList);
//                formData.append("vehicleRenewalList", JSON.stringify(props.selectedVehicleList));
//    dispatch({type: 'SUBMIT_BTN_LOADING', sBtnLoad: true})
//            vehicleRenewalService.uplaodSumInsured(formData, USR_TSK_MRINP_USIF).then(res =>

    @PostMapping(value = "/")
    public ResponseEntity<Boolean> uploadSumInsuredExcel(
            @RequestParam(required = false) MultipartFile file, @RequestParam("postDto") String postDto) throws JsonProcessingException {

            PostDtoEmpl postDtoEmpl = objectMapper.readValue(postDto, PostDtoEmpl.class);

        Employee employee = employeeRepository.findById(postDtoEmpl.getEmployeeId())
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });

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

    @GetMapping("/{empId}")
    public ResponseEntity<List<PostDto>> fetchPosts(@PathVariable String empId){


        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });

        List<PostDto> posts = postRepository.findAll().stream().
                map(obj -> {
                    long likeCount = obj.getReaction().stream().filter(lke -> lke.getReactionType()
                            .equals(ReactionType.LIKE)).count();
                    long heartCount = obj.getReaction().stream().filter(hrt -> hrt.getReactionType()
                            .equals(ReactionType.HEART)).count();

                    Optional<Reaction> employeeLike = obj.getReaction().stream().filter(lke -> lke.getReactionType()
                            .equals(ReactionType.LIKE) && lke.getEmployee().equals(employee)).findFirst();
                    Optional<Reaction> employeeHeart = obj.getReaction().stream().filter(lke -> lke.getReactionType()
                            .equals(ReactionType.HEART) && lke.getEmployee().equals(employee)).findFirst();

                    return PostDto.builder()
                    .id(obj.getId())
                    .postText(obj.getPostText())
                    .createdUser(obj.getEmployee().getName())
                    .postImageDestination(obj.getPostImageDestination())
                    .timeFrom(getDays(obj.getCreatedDateTime()))
                    .totalReactions(likeCount+heartCount)
                    .totalLikes(likeCount)
                    .totalHearts(heartCount)
                    .totalComments(0L)
                    .isHearted(employeeHeart.isPresent())
                    .isLiked(employeeLike.isPresent())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(posts);
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
            return StringUtils.FRONT_DESTINATION.concat(file.getOriginalFilename());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    @GetMapping("/react/{empId}/{postId}/{type}")
    public ResponseEntity<Boolean> reactPost(@PathVariable String empId, @PathVariable String postId,
                                              @PathVariable String type){

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Post not found");
                });

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

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Post not found");
                });

        Comment comment = Comment.builder().cmtTxt(commentDto.getCommentTxt())
                .employee(employee).post(post).createdDateTime(LocalDateTime.now()).build();

        commentRepository.save(comment);

        //send notification
        return ResponseEntity.ok(Boolean.TRUE);
    }




    }
