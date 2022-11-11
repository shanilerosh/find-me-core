package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class CommentDto {

    private String commentTxt;


}
