package com.findmecore.findmecore.controller;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Role;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import com.findmecore.findmecore.repo.RoleRepository;
import com.findmecore.findmecore.security.jwt.TokenProvider;
import com.findmecore.findmecore.service.EmployerService;
import com.findmecore.findmecore.service.UserService;
import com.findmecore.findmecore.utility.GeneralUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.findmecore.findmecore.entity.Role.ROLE_EMPLOYEE;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    EmployerService employeeService;

    @Autowired
    EmployerService employerService;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();

        User user = localUser.getUser();

        Role byName = roleRepository.findByName(ROLE_EMPLOYEE);

        boolean isEmployee = user.getRoles().contains(byName);

        if(isEmployee) {
            Employee employeeByUser = employeeService.findEmployeeByUser(user);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, GeneralUtils.buildUserInfo(localUser, employeeByUser)));
        }else {
            //EMployer Function
            return null;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            userService.registerNewUser(signUpRequest);
            return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));

        } catch (UserAlreadyExistAuthenticationException e) {
            log.error("Exception Ocurred", e);
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }
    }
}
