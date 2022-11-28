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

    public final static String CV_PROFILE_INFO = "I am a dedicated, organized and methodical individual. I have good interpersonal skills, am an excellent team worker and am keen and very willing to learn and develop new skills. I am reliable and dependable and often seek new responsibilities within a wide range of employment areas. I have an active and dynamic approach to work and getting things done. I am determined and decisive. I identify and develop opportunities.";
    public final static String CV_PROFILE_REMARK = "I’m a nice fun and friendly person, I’m honest and punctual, I work well in a team but also on my own as I like to set myself goals which I will achieve, I have good listening and communication skills. I have a creative mind and am always up for new challenges. I am well organized and always plan ahead to make sure I manage my time well.";
    public final static String CV_PROFILE_EXTRA = "While it is a good idea to also add co-curricular activities to your resume/CV, they are different from extracurricular activities. Co-curricular activities and experiences are ones that are more closely related to your chosen field of study, for example, sports, musical activities, debate, art, drama, debate, writing competition, etc. ";

}
