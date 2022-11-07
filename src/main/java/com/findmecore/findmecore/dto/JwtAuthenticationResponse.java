package com.findmecore.findmecore.dto;

import lombok.Value;

/**
 * @author ShanilErosh
 */
@Value
public class JwtAuthenticationResponse {
    private String accessToken;
    private UserInfo user;
}
