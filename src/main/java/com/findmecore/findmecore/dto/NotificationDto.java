package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ShanilErosh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {

    private String message;
    private String timeAgo;
    private boolean readStatus;
    private long notificationId;
    private String type;
    private long receiverId;
    private long creatorId;
    @JsonProperty("isRead")
    private boolean isRead;

    private String shooterProfilePic;
    private String shooterProfileName;

    private boolean receiverProfilePic;
    private boolean receiverProfileName;
}
