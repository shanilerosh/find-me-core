package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * @author ShanilErosh
 */
@Value
@Builder
public class UserInfo {
    private String id, displayName, email, userType, partyId, profilePic;
    private List<String> roles;
}
