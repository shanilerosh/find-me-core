package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class EmployeeDto implements Serializable {
    private Long employeeId;
    private String name;
    private Date dob;
    private String email;
    private String linkedinProfile;
    private String profilePicLocation;
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

    private List<CourseDto> courseList;
    private List<ExperienceDto> experienceDtos;
    private List<AbilityDto> abilityDtos;


    private String displayInstitute;
    private String currentCompany;
    private String currentPostion;

    private String listOfConnections;
    private String listOfPosts;
    private String listOfFriends;


    private String profileInformation;
    private String profileRemark;
    private String extra;
    private String theme;

}
