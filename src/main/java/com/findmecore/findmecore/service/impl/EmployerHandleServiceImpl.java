package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.EmailDto;
import com.findmecore.findmecore.dto.UserDto;
import com.findmecore.findmecore.entity.Employer;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.EmployerRepository;
import com.findmecore.findmecore.repo.UserRepository;
import com.findmecore.findmecore.service.EmailService;
import com.findmecore.findmecore.service.EmployerHandleService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ShanilErosh
 */
@Service
@Transactional
public class EmployerHandleServiceImpl implements EmployerHandleService
{

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean createEmployerWithUserData(UserDto userDto) {

        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> {
                    throw new UserAlreadyExistAuthenticationException("User not found");
                });

        Employer employer = Employer.builder().email(user.getEmail()).isUpdatedForTheFirstTime(false).user(user)
                .build();

        Employer savedEmployer = employerRepository.save(employer);

        user.setEmployer(savedEmployer);

        userRepository.save(user);
        return true;
    }

    @Override
    public Employer findByUser(User user) {

        return employerRepository.findByUser(user)
                .orElseThrow(()-> {
                    throw new RuntimeException("User not fuound");
                });
    }
}
