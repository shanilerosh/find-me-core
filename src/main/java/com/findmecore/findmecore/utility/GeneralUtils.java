package com.findmecore.findmecore.utility;

import com.findmecore.findmecore.dto.LocalUser;
import com.findmecore.findmecore.dto.SocialProvider;
import com.findmecore.findmecore.dto.UserInfo;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Employer;
import com.findmecore.findmecore.entity.Role;
import com.findmecore.findmecore.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
public class GeneralUtils {

    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public static SocialProvider toSocialProvider(String providerId) {
        for (SocialProvider socialProvider : SocialProvider.values()) {
            if (socialProvider.getProviderType().equals(providerId)) {
                return socialProvider;
            }
        }
        return SocialProvider.LOCAL;
    }

    public static UserInfo buildUserInfo(LocalUser localUser) {
        List<String> roles = localUser.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        User user = localUser.getUser();

        UserInfo build = UserInfo.builder().id(user.getId().toString()).email(user.getEmail())
                //.displayName(null == employee.getName() ? user.getEmail() : employee.getName())
                //.profilePic(employee.getProfilePicLocation())
                .userType("Employeer")
                .roles(roles).build();

        return build;

    }

    public static UserInfo buildUserInfo(LocalUser localUser, Employee employee) {
        List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = localUser.getUser();

        UserInfo build = UserInfo.builder().id(user.getId().toString())
                .partyId(employee.getEmployeeId().toString()).email(user.getEmail())
                .displayName(null == employee.getName() ? user.getEmail() : employee.getName()).profilePic(employee.getProfilePicLocation())
                .userType("Employee")
                .roles(roles).build();


        return build;
    }

    public static UserInfo buildUserInfo(LocalUser localUser, Employer employee) {
        List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = localUser.getUser();

        UserInfo build = UserInfo.builder().id(user.getId().toString())
                .partyId(employee.getEmployerId().toString()).email(user.getEmail())
                .displayName(null == employee.getName() ? user.getEmail() : employee.getName()).profilePic(employee.getProfilePicLocation())
                .userType("Employer")
                .roles(roles).build();

        return build;
    }
}
