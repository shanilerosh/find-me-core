package com.findmecore.findmecore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CompanyMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String registrationNumber;
    private String field;

    //Associates
    @OneToMany(mappedBy = "companyMst")
    Set<Experience> experiences;
}
