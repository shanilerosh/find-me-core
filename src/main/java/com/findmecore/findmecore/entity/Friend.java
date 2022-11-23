package com.findmecore.findmecore.entity;

import com.findmecore.findmecore.dto.FriendStatus;
import com.findmecore.findmecore.dto.ReactionType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateOfFriendShip;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Employee friend;

    @ManyToOne
    @JoinColumn(name = "current_id")
    private Employee currentEmployee;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;


}
