package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface EmployeeService {
    Boolean updateEmployeeBasicData(String empId, EmployeeDto employeeDto);

    EmployeeDto findEmployeeByUsername(String empId);

    List<ExperienceDto> fetchCompanyNames();

    List<CourseDto> fetchInstituteNames();

    ExperienceDto fetchExperienceRecord(String id);

    Boolean updateExperience(String expId, ExpUtil expUtil);

    Boolean updateEducation(String expId, EdUtil expUtil);

    Boolean createExperience(String empId, ExpUtil expUtil);

    Boolean createEducation(String empId, EdUtil expUtil);

    CourseDto fetchCourseRecord(String id);

    Boolean createEmployeeWithUserData(UserDto userDto);
}
