package com.findmecore.findmecore.controller;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.exceptions.ResourceNotFoundException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.utility.GeneralUtils;
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
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    @PostMapping("/{empId}")
    public ResponseEntity<Boolean> updateEmployeeImg(@PathVariable String empId, @RequestParam("image")MultipartFile multipartFile) {
        return null;

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
                .id(obj.getId())
                .isDisplayMajor(obj.isDisplayMajor())
                .build()).collect(Collectors.toList());

        List<ExperienceDto> experienceList = employee.getExperiences().stream().map(obj -> ExperienceDto.builder().company(obj.getCompanyMst().getCompanyName())
                .position(obj.getPosition())
                .started(obj.getStarted())
                .isCurrent(obj.isCurrent())
                .ended(obj.getEnded())
                .id(obj.getId())
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

    @GetMapping("/company")
    public List<ExperienceDto> fetchCompanyNames() {
            return companyRepository.findAll().stream()
                    .map(obj -> ExperienceDto.builder().company(obj.getCompanyName()).id(obj.getId())
                            .build()).collect(Collectors.toList());
    }

    @GetMapping("/institute")
    public List<CourseDto> fetchInstituteNames() {
        return instituteRepository.findAll().stream()
                .map(obj -> CourseDto.builder().institute(obj.getInstitute()).id(obj.getId())
                        .build()).collect(Collectors.toList());
    }

    @GetMapping("/experience/{id}")
    public ResponseEntity<ExperienceDto> fetchExperienceRecord(@PathVariable String id) {
        Experience experience = experienceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        ExperienceDto experienceDto = ExperienceDto.builder().isCurrent(experience.isCurrent())
                .companyId(experience.getCompanyMst().getId())
                .company(experience.getCompanyMst().getCompanyName())
                .started(experience.getStarted())
                .ended(experience.getEnded())
                .position(experience.getPosition())
                .build();

        return ResponseEntity.ok(experienceDto);

    }

    @GetMapping("/education/{id}")
    public ResponseEntity<CourseDto> fetchCourseRecord(@PathVariable String id) {
        Course course = courseRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        CourseDto courseDto = CourseDto.builder().courseName(course.getCourseName())
                .instituteId(course.getEducation().getId())
                .isDisplayMajor(course.isDisplayMajor())
                .started(course.getStarted())
                .ended(course.getEnded())
                .build();
        return ResponseEntity.ok(courseDto);
    }

    @PutMapping("/experience/{expId}")
    public ResponseEntity<Boolean> updateExperience(@PathVariable String expId, @RequestBody ExpUtil expUtil) {

        Experience experience = experienceRepository.findById(Long.valueOf(expId))
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        CompanyMst companyMst = companyRepository.findById(expUtil.getCompany())
                .orElseThrow(() -> {
                    throw new BadRequestException("No company found with the record");
                });

        experience.setCompanyMst(companyMst);
        experience.setCurrent(expUtil.isCurrentStatus());
        experience.setPosition(expUtil.getPosition());
        experience.setStarted(expUtil.getStartDate());
        experience.setEnded(expUtil.getEndDate());

        experienceRepository.save(experience);

        return ResponseEntity.ok(Boolean.TRUE);

    }

    @PutMapping("/education/{expId}")
    public ResponseEntity<Boolean> updateEducation(@PathVariable String expId, @RequestBody EdUtil expUtil) {

        Course course = courseRepository.findById(Long.valueOf(expId))
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        Education education = instituteRepository.findById(expUtil.getInstitute())
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        course.setEducation(education);
        course.setStarted(expUtil.getStarted());
        course.setEnded(expUtil.getEnded());
        course.setCourseName(expUtil.getCourseName());
        course.setDisplayMajor(expUtil.isDisplayMajor());

        courseRepository.save(course);
        return ResponseEntity.ok(Boolean.TRUE);
    }


    @PostMapping("/experience/{empId}")
    public ResponseEntity<Boolean> createExperience(@PathVariable String empId, @RequestBody ExpUtil expUtil) {

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new BadRequestException("No employee found with the record");
                });

        CompanyMst companyMst = companyRepository.findById(expUtil.getCompany())
                .orElseThrow(() -> {
                    throw new BadRequestException("No company found with the record");
                });

        Experience exp = Experience.builder().employee(employee)
                .companyMst(companyMst)
                .ended(expUtil.getEndDate())
                .started(expUtil.getStartDate())
                .position(expUtil.getPosition())
                .isCurrent(expUtil.isCurrentStatus())
                .build();

        experienceRepository.save(exp);

        return ResponseEntity.ok(Boolean.TRUE);

    }

    @PostMapping("/education/{empId}")
    public ResponseEntity<Boolean> createEducation(@PathVariable String empId, @RequestBody EdUtil expUtil) {

        Employee employee = employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new BadRequestException("No employee found with the record");
                });

        Education education = instituteRepository.findById(expUtil.getInstitute())
                .orElseThrow(() -> {
                    throw new BadRequestException("No experience found with the record");
                });

        Course course = Course.builder().education(education)
                .employee(employee).started(expUtil.getStarted())
                .ended(expUtil.getEnded())
                .courseName(expUtil.getCourseName())
                .isDisplayMajor(expUtil.isDisplayMajor()).build();

        courseRepository.save(course);

        return ResponseEntity.ok(Boolean.TRUE);

    }




}
