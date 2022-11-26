package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class AbilityDto {

    private Long id;
    private String skillName;
    private long rating;
    private String empId;
    private long skillId;
}
