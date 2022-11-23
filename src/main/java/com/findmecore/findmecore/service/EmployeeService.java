package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface EmployeeService {
    Boolean updateEmployeeBasicData(String empId, EmployeeDto employeeDto, MultipartFile file);

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

    String generateCv(String empId, CvUtilDto cvUtilDto);

    List<SkillDto> generateSkills();

    Boolean updateSkill(String empId, String skillId, SkillUtilDto skillUtilDto);

    Boolean createSkill(String skillId, SkillUtilDto skillUtilDto);
}
