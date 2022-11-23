package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class SkillUtilDto implements Serializable {

    private Long skillId;
    private String skillName;
    private long rating;
}
