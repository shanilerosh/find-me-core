package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class FriendCommonDto implements Serializable {

    private Long friendId;
    private String friendName;
    private String friendPhoto;
    private String friendPics;
    private Long friendEmpId;
    private String friendEmail;
    private String friendInfo;
    private String appliedOn;
    private boolean isFriend;


}
