package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.MessageDto;
import com.findmecore.findmecore.dto.UserDto;
import com.findmecore.findmecore.entity.Employer;
import com.findmecore.findmecore.entity.User;

/**
 * @author ShanilErosh
 */
public interface EmployerHandleService {

    Boolean createEmployerWithUserData(UserDto userDto);

    Employer findByUser(User user);

}
