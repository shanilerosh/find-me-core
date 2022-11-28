package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class EmployerDto implements Serializable {

    private String name;
    private String email;
    private String registrationId;
    private String linkedinProfile;
    private String profileInfo;
    private String profilePicLocation;
}
