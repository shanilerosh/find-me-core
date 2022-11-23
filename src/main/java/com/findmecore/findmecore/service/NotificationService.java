package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.LocalUser;
import com.findmecore.findmecore.dto.NotificationDto;
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
public interface NotificationService {

    void createNotification(NotificationDto notificationDto);

}
