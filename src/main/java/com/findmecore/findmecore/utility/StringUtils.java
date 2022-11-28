package com.findmecore.findmecore.utility;

import com.findmecore.findmecore.dto.LocalUser;
import com.findmecore.findmecore.dto.SocialProvider;
import com.findmecore.findmecore.dto.UserInfo;
import com.findmecore.findmecore.entity.Role;
import com.findmecore.findmecore.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
public class StringUtils {

    public static String IMAGE_SAVE_LOCATION_POST = "C:\\Users\\Admin\\Desktop\\Final Project FInalization\\argon-dashboard-angular-master\\src\\assets\\img\\posts\\";
    public static String IMAGE_SAVE_LOCATION_PDF_UTIL = "C:\\Users\\Admin\\Desktop\\Final Project FInalization\\argon-dashboard-angular-master\\src\\";
    public static String FRONT_DESTINATION = "assets\\img\\posts\\";


    //new customer email
    public final static String NEW_CUSTOMER_EMAIL_BODY = "" +
            "<p>Dear Sir/Madam, </p> <br> <p>Welcome to Find Me Web Application. Thank you for registering as a new user.</p> <br>\n" +
            "    \n" +
            "\n" +
            " <p>Thanks & regards</p> <br><br><br><br><p style=\"font-size:11px;text-align:center\">This is an automatically generated e-mail from our System. Please do not reply to this e-mail.</p>";
}
