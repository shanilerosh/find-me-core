package com.findmecore.findmecore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private LocalDateTime started;
    private LocalDateTime ended;

    private String position;

    private boolean isCurrent;
}
