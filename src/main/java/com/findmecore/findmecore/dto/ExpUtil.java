package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class ExpUtil implements Serializable {

    private Long company;

    @JsonProperty("currentStatus")
    private boolean currentStatus;

    private Date endDate;
    private Date startDate;

    private String  position;

    @JsonProperty("isDisplayMajor")
    private boolean isDisplayMajor;
}
