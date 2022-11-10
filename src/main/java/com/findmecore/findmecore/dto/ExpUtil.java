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
public class ExpUtil {

    private Long company;

    @JsonProperty("currentStatus")
    private boolean currentStatus;

    private LocalDateTime endDate;
    private LocalDateTime startDate;

    private String  position;

    @JsonProperty("isDisplayMajor")
    private boolean isDisplayMajor;
}
