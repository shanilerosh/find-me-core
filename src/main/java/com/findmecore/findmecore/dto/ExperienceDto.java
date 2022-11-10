package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class ExperienceDto {

    private Long id;
    private String company;
    private LocalDateTime started;
    private LocalDateTime ended;

    private Long companyId;

    private String position;
    @JsonProperty("isCurrent")
    private boolean isCurrent;
}
