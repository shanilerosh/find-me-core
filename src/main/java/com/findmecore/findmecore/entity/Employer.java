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
public class Employer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;
    private String name;
    private String email;
    private String registrationId;
    private String linkedinProfile;
    private String profileInfo;
    private String profilePicLocation;


    @OneToMany(mappedBy = "employer")
    Set<Connection> connections;

    private Boolean isUpdatedForTheFirstTime;
}
