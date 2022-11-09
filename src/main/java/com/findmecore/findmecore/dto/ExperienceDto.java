package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

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

    private String position;

    private boolean isCurrent;
}
