package com.findmecore.findmecore.dto;

import com.findmecore.findmecore.validator.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author ShanilErosh
 */
public enum SocialProvider {

    GOOGLE("google"), LOCAL("local");

    private String providerType;

    public String getProviderType() {
        return providerType;
    }

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }
}
