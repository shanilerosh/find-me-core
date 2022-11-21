package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class UserDto implements Serializable {

    private Long userId;
    private String email;
    private String diplayName;


}
