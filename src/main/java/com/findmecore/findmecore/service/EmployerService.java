package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.User;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface EmployerService {

    Boolean createEmployerWithUserData(UserDto userDto);

    Employee findEmployeeByUser(User user);
}
