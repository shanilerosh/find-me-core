package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class FriendDto implements Serializable {

    private Long friendId;
    private String friendName;
    private String friendsSince;


}
