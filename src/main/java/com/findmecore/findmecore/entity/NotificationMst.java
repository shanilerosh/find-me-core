package com.findmecore.findmecore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class NotificationMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notificationData;
    private String relatedParty;
    private LocalDateTime createdDate;

    private Boolean isReadStatus;

}
