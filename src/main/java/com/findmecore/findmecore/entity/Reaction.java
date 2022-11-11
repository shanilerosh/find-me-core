package com.findmecore.findmecore.entity;

import com.findmecore.findmecore.dto.PostVisibility;
import com.findmecore.findmecore.dto.ReactionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDateTime;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


}
