package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.LocalUser;
import com.findmecore.findmecore.dto.SignUpRequest;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface UserService {
    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

}
