package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CvUtilDto implements Serializable {

    private String profileInfo;
    private String remark;
    private String extra;
    private String theme;
}
