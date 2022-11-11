package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoEmpl implements Serializable {

    private long employeeId;
    private String postText;
    private String visibility;
}
