package com.findmecore.findmecore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ShanilErosh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatFrontDto implements Serializable {

        private Long id;
        private String userImage;
        private String timeAgo;
        @JsonProperty("isLoggedToTarget")
        private boolean isLoggedToTarget;
        private String chatText;
        private String username;

    @JsonProperty("isRecent")
    private boolean isRecent;
}
