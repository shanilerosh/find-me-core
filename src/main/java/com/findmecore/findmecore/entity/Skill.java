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
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skillName;
    private String field;

    @OneToMany(mappedBy = "skill")
    Set<Ability> abilities;
}
