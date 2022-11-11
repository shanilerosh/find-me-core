package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private String timeFrom;
    private Long totalReactions;
    private Long totalLikes;
    private Long totalHearts;
    private Long totalComments;

    @JsonProperty("isLiked")
    private Boolean isLiked;
    @JsonProperty("isHearted")
    private Boolean isHearted;
}
