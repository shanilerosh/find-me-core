package com.findmecore.findmecore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_Id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private LocalDateTime appliedDate;
}
