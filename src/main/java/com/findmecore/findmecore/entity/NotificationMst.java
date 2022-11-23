package com.findmecore.findmecore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationId;
    private String message;
    private LocalDateTime timeAgo;
    private boolean readStatus;
    private String type;
    private long receiverId;
    private long creatorId;
    private boolean isRead;

    private boolean shooterProfilePic;
    private boolean shooterProfileName;

    private boolean receiverProfilePic;
    private boolean receiverProfileName;

}
