package com.findmecore.findmecore.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String name;
    private Date dob;
    private String email;
    private String linkedinProfile;
    private String address;
    private String mobile;

    private String intro;


    //ref data
    private String ref1;
    private String ref1Address;
    private String ref1Mobile;
    private String ref1Position;

    private String ref2;
    private String ref2Address;
    private String ref2Mobile;
    private String ref2Position;


    private String country;
    private String city;
    private String town;
    private String aboutMe;


    //Associates
    @OneToMany(mappedBy = "employee")
    Set<Course> course;

    @OneToMany(mappedBy = "employee")
    Set<Experience> experiences;

    @OneToMany(mappedBy = "employee")
    Set<Ability> ability;

    @OneToMany(mappedBy = "employee")
    Set<Post> post;

    @OneToMany(mappedBy = "employee")
    Set<Reaction> reaction;

    @OneToMany(mappedBy = "employee")
    Set<Comment> comment;


    private String profilePicLocation;


}
