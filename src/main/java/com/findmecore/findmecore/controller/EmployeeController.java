package com.findmecore.findmecore.controller;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Course;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.utility.GeneralUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PutMapping("/{empId}")
    public ResponseEntity<Boolean> updateEmployeeBasicData(@PathVariable String empId, @RequestBody EmployeeDto employeeDto) {

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });

        employee.setAboutMe(employeeDto.getAboutMe());
        employee.setAddress(employeeDto.getAddress());
        employee.setCity(employeeDto.getCity());
        employee.setCountry(employeeDto.getCountry());
        employee.setMobile(employeeDto.getMobile());
        employee.setName(employeeDto.getName());
        employee.setTown(employeeDto.getTown());

        employeeRepository.save(employee);

        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDto> findEmployeeByUsername(@PathVariable String empId) {

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(()-> {
                    throw new ResolutionException("Employee not found");
                });


        EmployeeDto build = EmployeeDto.builder().build();
        BeanUtils.copyProperties(employee, build);

        List<CourseDto> courseList = employee.getCourse().stream().map(obj -> CourseDto.builder().courseName(obj.getCourseName())
                .institute(obj.getEducation().getInstitute())
                .started(obj.getStarted())
                .ended(obj.getEnded())
                .isDisplayMajor(obj.isDisplayMajor())
                .build()).collect(Collectors.toList());

        List<ExperienceDto> experienceList = employee.getExperiences().stream().map(obj -> ExperienceDto.builder().company(obj.getCompanyMst().getCompanyName())
                .position(obj.getPosition())
                .started(obj.getStarted())
                .isCurrent(obj.isCurrent())
                .ended(obj.getEnded())
                .build()).collect(Collectors.toList());


        //find the current company
        Optional<ExperienceDto> currentExperiece = experienceList.stream().filter(ExperienceDto::isCurrent).findFirst();


        build.setCurrentCompany(currentExperiece.isPresent() ? currentExperiece.get().getCompany() : "Unemployed");
        build.setCurrentPostion(currentExperiece.isPresent() ? currentExperiece.get().getPosition() : "Unemployed");

        Optional<CourseDto> currentMajor = courseList.stream().filter(CourseDto::isDisplayMajor).findFirst();

        build.setDisplayInstitute(currentMajor.isPresent() ? currentMajor.get().getInstitute() : "");

        build.setCourseList(courseList);
        build.setExperienceDtos(experienceList);

        return ResponseEntity.ok(build);
    }

}
