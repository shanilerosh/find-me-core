package com.findmecore.findmecore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyMst companyMst;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Date started;
    private Date ended;

    private String position;

    private boolean isCurrent;
}
