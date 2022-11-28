package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.EmailDto;
import com.findmecore.findmecore.dto.NotificationDto;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface EmailService {

    void createEmail(EmailDto emailDto);
}
