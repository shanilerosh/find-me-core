package com.findmecore.findmecore.service;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    String generateCv(String empId, CvUtilDto cvUtilDto) throws IOException;

    List<SkillDto> generateSkills();

    Boolean updateSkill(String empId, String skillId, SkillUtilDto skillUtilDto);

    Boolean createSkill(String skillId, SkillUtilDto skillUtilDto);

    List<FriendCommonDto> filterFriends(String empId, String status);

    Boolean acceptRejectFriendship(String friendshipId, String status);

    List<FriendCommonDto> findFriendsByEmployeeAndStatus(Employee employeeById, FriendStatus status);

    Boolean createAbiltiy(String empId, AbilityDto abilityDto);

    Boolean updateAbility(String abilityId, AbilityDto abilityDto);

    Boolean deleteAbility(String abilityId);

    AbilityDto fetchAbiltiyRecord(String id);
}
