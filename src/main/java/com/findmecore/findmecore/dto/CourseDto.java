package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.findmecore.findmecore.entity.Education;
import com.findmecore.findmecore.entity.Employee;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class CourseDto {

    private Long id;

    private String institute;
    private Long instituteId;

    private LocalDateTime started;
    private LocalDateTime ended;

    private String courseName;

    @JsonProperty("isDisplayMajor")
    private boolean isDisplayMajor;
}
