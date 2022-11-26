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
    private String party;
    private LocalDateTime timeAgo;
    private boolean readStatus;
    private String type;
    private long receiverId;
    private long creatorId;
    @Column(nullable=false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRead;

    private String shooterProfilePic;
    private String shooterProfileName;

    private String receiverProfilePic;
    private String receiverProfileName;

    private boolean isPositive;

}
