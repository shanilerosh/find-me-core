package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class SkillDto {

    private Long id;
    private String skillName;
}
