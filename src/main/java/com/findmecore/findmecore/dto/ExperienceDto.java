package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class ExperienceDto {

    private Long id;
    private String company;
    private Date started;
    private Date ended;

    private Long companyId;

    private String position;
    @JsonProperty("isCurrent")
    private boolean isCurrent;
}
