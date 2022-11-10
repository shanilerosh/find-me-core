package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class EdUtil {

    private Long institute;
    private LocalDateTime ended;
    private LocalDateTime started;
    private String  courseName;
    @JsonProperty("isDisplayMajor")
    private boolean isDisplayMajor;
}
