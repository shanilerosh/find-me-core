package com.findmecore.findmecore.controller;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.exceptions.ResourceNotFoundException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import com.findmecore.findmecore.utility.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PutMapping("/{empId}")
    public ResponseEntity<Boolean> updateEmployeeBasicData(@PathVariable String empId, @RequestBody EmployeeDto employeeDto) {

        return ResponseEntity.ok(employeeService.updateEmployeeBasicData(empId, employeeDto));

    }


    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDto> findEmployeeByUsername(@PathVariable String empId) {

        return ResponseEntity.ok(employeeService.findEmployeeByUsername(empId));
    }

    @GetMapping("/company")
    public List<ExperienceDto> fetchCompanyNames() {
            return employeeService.fetchCompanyNames();
    }

    @GetMapping("/institute")
    public List<CourseDto> fetchInstituteNames() {
        return employeeService.fetchInstituteNames();
    }

    @GetMapping("/experience/{id}")
    public ResponseEntity<ExperienceDto> fetchExperienceRecord(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.fetchExperienceRecord(id));

    }

    @GetMapping("/education/{id}")
    public ResponseEntity<CourseDto> fetchCourseRecord(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.fetchCourseRecord(id));
    }

    @PutMapping("/experience/{expId}")
    public ResponseEntity<Boolean> updateExperience(@PathVariable String expId, @RequestBody ExpUtil expUtil) {

        return ResponseEntity.ok(employeeService.updateExperience(expId, expUtil));

    }

    @PutMapping("/education/{expId}")
    public ResponseEntity<Boolean> updateEducation(@PathVariable String expId, @RequestBody EdUtil expUtil) {
        return ResponseEntity.ok(employeeService.updateEducation(expId, expUtil));
    }


    @PostMapping("/experience/{empId}")
    public ResponseEntity<Boolean> createExperience(@PathVariable String empId, @RequestBody ExpUtil expUtil) {
        return ResponseEntity.ok(employeeService.createExperience(empId, expUtil));

    }

    @PostMapping("/education/{empId}")
    public ResponseEntity<Boolean> createEducation(@PathVariable String empId, @RequestBody EdUtil expUtil) {
        return ResponseEntity.ok(employeeService.createEducation(empId, expUtil));

    }




}
