package com.findmecore.findmecore.security.oAuth2.user;

import com.findmecore.findmecore.dto.SocialProvider;
import com.findmecore.findmecore.exceptions.OAuth2AuthenticationProcessingException;

import java.util.Map;

/**
 * @author ShanilErosh
 */
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(SocialProvider.GOOGLE.getProviderType())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
