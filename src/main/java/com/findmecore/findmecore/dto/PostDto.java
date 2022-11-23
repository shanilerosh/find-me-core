package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class PostDto implements Serializable {

    private Long id;
    private String createdUser;
    private String postText;
    private String postImageDestination;
    private String userImageDestination;
    private String timeFrom;
    private Long totalReactions;
    private Long totalComments;
    private Long totalLikes;
    private Long totalHearts;
    private String userImg;
    private String reactionCommentCount;

    @JsonProperty("isLiked")
    private Boolean isLiked;
    @JsonProperty("isHearted")
    private Boolean isHearted;

    private List<ReactDto> likeList;
    private List<ReactDto> heartedList;
    private List<CommentFrontDto> listOfComments;
}
