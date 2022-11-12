package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class CommentFrontDto {

    private String userProfileImg;
    private String username;
    private String timeAgo;
    private String commentText;

}
