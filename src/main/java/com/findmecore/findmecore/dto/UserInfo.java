package com.findmecore.findmecore.dto;

import lombok.Value;

import java.util.List;

/**
 * @author ShanilErosh
 */
@Value
public class UserInfo {
    private String id, displayName, email;
    private List<String> roles;
}
