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
public class ReactDto {

    private String reactType;

    @JsonProperty("isClick")
        private boolean isClick;

}
