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
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institute;

    @OneToMany(mappedBy = "education")
    Set<Course> education;
}
