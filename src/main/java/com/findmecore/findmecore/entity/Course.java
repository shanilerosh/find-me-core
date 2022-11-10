package com.findmecore.findmecore.entity;

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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "education_id")
    private Education education;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDateTime started;
    private LocalDateTime ended;

    private String courseName;

    private boolean isDisplayMajor;
}
