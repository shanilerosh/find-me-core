package com.findmecore.findmecore.dto;

/**
 * @author ShanilErosh
 */
public enum CvThemeProvider {

    DARK("dark"),LIGHT("light"),PROFESSIONAL("professional");

    private String providerType;

    public String getProviderType() {
        return providerType;
    }

    CvThemeProvider(final String providerType) {
        this.providerType = providerType;
    }
}
