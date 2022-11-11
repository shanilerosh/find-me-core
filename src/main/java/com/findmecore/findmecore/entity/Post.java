package com.findmecore.findmecore.entity;

import com.findmecore.findmecore.dto.PostVisibility;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postText;
    private LocalDateTime createdDateTime;
    private String postImageDestination;

    @Enumerated(EnumType.STRING)
    private PostVisibility postVisibility;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Boolean isJob;

    @OneToMany(mappedBy = "post")
    Set<Reaction> reaction;

    @OneToMany(mappedBy = "post")
    Set<Comment> comment;


}
