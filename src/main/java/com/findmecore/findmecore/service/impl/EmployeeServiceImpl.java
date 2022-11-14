package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

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

    @Override
    public Boolean updateEmployeeBasicData(String empId, EmployeeDto employeeDto) {
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
        return Boolean.TRUE;
    }

    @Override
    public EmployeeDto findEmployeeByUsername(String empId) {
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

        return build;
    }

    @Override
    public List<ExperienceDto> fetchCompanyNames() {
        return companyRepository.findAll().stream()
                .map(obj -> ExperienceDto.builder().company(obj.getCompanyName()).id(obj.getId())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> fetchInstituteNames() {
        return instituteRepository.findAll().stream()
                .map(obj -> CourseDto.builder().institute(obj.getInstitute()).id(obj.getId())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public ExperienceDto fetchExperienceRecord(String id) {
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

        return experienceDto;
    }

    @Override
    public Boolean updateExperience(String expId, ExpUtil expUtil) {

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

        return Boolean.TRUE;
    }

    @Override
    public Boolean updateEducation(String expId, EdUtil expUtil) {
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

        return Boolean.TRUE;
    }

    @Override
    public Boolean createExperience(String empId, ExpUtil expUtil) {
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

        return Boolean.TRUE;
    }

    @Override
    public Boolean createEducation(String empId, EdUtil expUtil) {
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

        return Boolean.TRUE;
    }

    @Override
    public CourseDto fetchCourseRecord(String id) {
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

        return courseDto;
    }
}
