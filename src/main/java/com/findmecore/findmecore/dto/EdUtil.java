package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class EdUtil implements Serializable {

    private Long institute;
    private Date ended;
    private Date started;
    private String  courseName;
    @JsonProperty("isDisplayMajor")
    private boolean isDisplayMajor;
}
