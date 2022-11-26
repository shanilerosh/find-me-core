package com.findmecore.findmecore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import com.findmecore.findmecore.service.NotificationService;
import com.findmecore.findmecore.service.PostService;
import com.findmecore.findmecore.utility.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
public class PostServiceImpl implements PostService {


    @Autowired
    private FriendRepository friendRepository;

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
    private NotificationService notificationService;



    @Override
    public Boolean createPost(MultipartFile file, PostDtoEmpl postDto) {

        Employee employee = findEmployeeById(postDto.getEmployeeId());
        String path = null;

        if(null != file) {
            path = saveFile(file);
        }

        Post post = Post.builder().employee(employee).postText(postDto.getPostText())
                .createdDateTime(LocalDateTime.now()).isJob(false).postVisibility(PostVisibility.valueOf(postDto
                        .getVisibility())).postImageDestination(path).build();
        postRepository.save(post);

        return Boolean.TRUE;
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

    @Override
    public List<PostDto> fetchPosts(String empId) {
        Employee employee = findEmployeeById(empId);

        List<PostDto> posts = findPostsByEmployee(employee);

        return posts;
    }

    private List<PostDto> findPostsByEmployee(Employee employee) {
        return postRepository.findAllByEmployee(employee).stream().sorted(Comparator.comparing(Post::getCreatedDateTime)
                .reversed()).
                map(obj -> getPostsDto(employee, obj)).collect(Collectors.toList());
    }

    private List<FriendDto> getCurrentFriendListOfAnEmployee(Employee employee) {
        return friendRepository.findAllByCurrentEmployeeAndFriendStatus(employee, FriendStatus.FRIENDS)
                .stream().map(obj -> FriendDto.builder().friendId(obj.getFriend().getEmployeeId()).friendName(obj.getFriend().getName()).friendsSince(getDays(obj.getDateOfFriendShip())).build())
                .collect(Collectors.toList());
    }

    private FriendCommonDto getFriendCommonDto(Friend obj) {
        Employee friendProfile = obj.getFriend();
        return FriendCommonDto.builder()
                .friendId(obj.getId()).friendEmpId(friendProfile.getEmployeeId()).
                        friendInfo(friendProfile.getAboutMe()).isFriend(obj.getFriendStatus().equals(FriendStatus.FRIENDS))
                .friendPhoto(friendProfile.getProfilePicLocation())
                .friendName(friendProfile.getName())
                .friendEmail(friendProfile.getEmail())
                .build();
    }

    private FriendCommonDto getFriendCommonDto(Friend obj, boolean flag) {
        Employee friendProfile = obj.getCurrentEmployee();
        return FriendCommonDto.builder()
                .friendId(obj.getId()).friendEmpId(friendProfile.getEmployeeId()).
                        friendInfo(friendProfile.getAboutMe()).isFriend(obj.getFriendStatus().equals(FriendStatus.FRIENDS))
                .friendPhoto(friendProfile.getProfilePicLocation())
                .friendName(friendProfile.getName())
                .friendEmail(friendProfile.getEmail())
                .build();
    }


    public List<FriendCommonDto> findFriendsByEmployeeAndStatus(Employee employeeById, FriendStatus status) {
        List<FriendCommonDto> friendList = friendRepository.findAllByCurrentEmployeeAndFriendStatus(employeeById, status)
                .stream().map(this::getFriendCommonDto).collect(Collectors.toList());

        //f both are friends should be taken from other side
        if(status == FriendStatus.FRIENDS) {

            List<FriendCommonDto> otherFriendSet = friendRepository.findAllByFriendAndFriendStatus(employeeById, status)
                    .stream().map(obj -> getFriendCommonDto(obj, false)).collect(Collectors.toList());

            LinkedHashSet<FriendCommonDto> hashSet = new LinkedHashSet<>(friendList);
            hashSet.addAll(otherFriendSet);

            return new ArrayList<>(hashSet);
        }
        return friendList;
    }

    private List<PostDto> findPostsByEmployeeFriends(Employee employee) {


        List<FriendCommonDto> friendsByEmployeeAndStatus = findFriendsByEmployeeAndStatus(employee, FriendStatus.FRIENDS);


        return postRepository.findAll().stream().sorted(Comparator.comparing(Post::getCreatedDateTime)
                .reversed()).
                filter(obj -> friendsByEmployeeAndStatus.stream().anyMatch(friend -> !friend.getFriendEmpId().equals(employee.getEmployeeId()) && friend.getFriendEmpId().equals(obj.getEmployee().getEmployeeId())))
                .map(obj -> getPostsDto(employee, obj)).
                collect(Collectors.toList());
    }

    private PostDto getPostsDto(Employee employee, Post obj) {
        List<ReactDto> likeList = getListOfReaction(obj, ReactionType.LIKE);

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
                .totalReactions(((long) likeList.size() + (long) heartList.size()))
                .totalLikes((long) likeList.size())
                .totalHearts((long) heartList.size())
                .isHearted(employeeHeart.isPresent())
                .isLiked(employeeLike.isPresent())
                .userImg(obj.getEmployee().getProfilePicLocation())
                .likeList(likeList)
                .heartedList(heartList)
                .listOfComments(listOfcomments)
                .totalComments((long) listOfcomments.size())
                .userImageDestination(employee.getProfilePicLocation())
                .reactionCommentCount(getReactionList(obj.getReaction(), obj.getComment()))
                .build();
    }

    @Override
    public Boolean reactPost(String empId, String postId, String type) {
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

        //create notification
        NotificationDto notificationDto = NotificationDto.builder().message(employee.getName() + " has reacted to your post!!")
                .isRead(false).receiverId(post.getEmployee().getEmployeeId())
                .creatorId(employee.getEmployeeId())
                .shooterProfileName(employee.getName())
                .shooterProfilePic(employee.getProfilePicLocation())
                .party(Role.ROLE_EMPLOYEE)
                .build();
        notificationService.createNotification(notificationDto);
        return Boolean.TRUE;
    }

    @Override
    public Boolean commentPost(String empId, String postId, CommentDto commentDto) {
        Employee employee = findEmployeeById(Long.valueOf(empId));

        Post post = fetchPostById(postId);

        Comment comment = Comment.builder().cmtTxt(commentDto.getCommentTxt())
                .employee(employee).post(post).createdDateTime(LocalDateTime.now()).build();

        commentRepository.save(comment);

        return Boolean.TRUE;
    }

    private Post fetchPostById(String postId) {
        return postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Post not found");
                });
    }

    @Override
    public List<CommentFrontDto> getComments(String postId) {
        Post post = fetchPostById(postId);
        return getListOfComments(post);
    }

    @Override
    public List<PostDto> fetchPostsByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<PostDto> fetchFriendsPosts(String empId) {
        Employee employee = findEmployeeById(empId);
        return findPostsByEmployeeFriends(employee);
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

    private String getReactionList(Set<Reaction> reactionSet, Set<Comment> commetSet) {
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

}
