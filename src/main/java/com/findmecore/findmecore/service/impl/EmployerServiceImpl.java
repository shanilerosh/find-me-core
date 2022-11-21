package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.exceptions.ResourceNotFoundException;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import com.findmecore.findmecore.service.EmployerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Boolean createEmployerWithUserData(UserDto userDto) {
        return null;
    }

    @Override
    public Employee findEmployeeByUser(User user) {
        Employee employee = employeeRepository
                .findByUser(user)
                .orElseThrow(() -> {
                    throw new RuntimeException("Not found");
                });

        return employee;
    }
}
