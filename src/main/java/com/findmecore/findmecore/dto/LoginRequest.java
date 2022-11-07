package com.findmecore.findmecore.dto;

import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;

/**
 * @author ShanilErosh
 */
@Data
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
