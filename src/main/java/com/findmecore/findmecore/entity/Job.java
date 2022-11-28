package com.findmecore.findmecore.entity;

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
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private Date expiryDate;
    @Column(columnDefinition = "TEXT")
    private String jobDescription;
    private String jobTitle;
    private String status;
    private String jobType;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Employer employer;

    @OneToMany(mappedBy = "job")
    Set<JobEmployee> jobEmployees;

}
