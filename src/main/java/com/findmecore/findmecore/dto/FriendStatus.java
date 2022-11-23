package com.findmecore.findmecore.dto;

/**
 * @author ShanilErosh
 */
public enum FriendStatus {

    REQUESTED("REQUESTED"),FRIENDS("FRIENDS");

    private String providerType;

    public String getProviderType() {
        return providerType;
    }

    FriendStatus(final String providerType) {
        this.providerType = providerType;
    }
}
