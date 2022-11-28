package com.findmecore.findmecore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper mapper;

    @PutMapping("/{empId}")
    public ResponseEntity<Boolean> updateEmployeeBasicData(
            @PathVariable String empId,@RequestParam(required = false)
                    MultipartFile file, @RequestParam("employeeDto") String employeeDto) throws JsonProcessingException {

        EmployeeDto dto = mapper.readValue(employeeDto, EmployeeDto.class);
        return ResponseEntity.ok(employeeService.updateEmployeeBasicData(empId, dto, file));

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

    @PostMapping("/cv/{empId}")
    public ResponseEntity<String> generateCv(@PathVariable String empId, @RequestBody CvUtilDto cvUtilDto) throws IOException {
        return ResponseEntity.ok(employeeService.generateCv(empId, cvUtilDto));
    }

    @GetMapping("/cv/{empId}")
    public ResponseEntity<String> generateCvDefault(@PathVariable String empId) throws IOException {
        return ResponseEntity.ok(employeeService.generateCv(empId, null));

    }


    @GetMapping("/skills")
    public ResponseEntity<List<SkillDto>> generateSkills() {
        return ResponseEntity.ok(employeeService.generateSkills());
    }


    @PutMapping("/skill/{empId}/{skillId}")
    public ResponseEntity<Boolean> updateSkill(@PathVariable String empId, @PathVariable String skillId, @RequestBody SkillUtilDto skillUtilDto) {
        return ResponseEntity.ok(employeeService.updateSkill(empId,skillId, skillUtilDto));
    }


    @PostMapping("/skill/{empId}")
    public ResponseEntity<Boolean> createSkill(@PathVariable String empId, @RequestBody SkillUtilDto skillUtilDto) {
        return ResponseEntity.ok(employeeService.createSkill(empId, skillUtilDto));

    }


    @GetMapping("/friend/{empId}/{status}")
    public ResponseEntity<List<FriendCommonDto>> filterFriends(@PathVariable String empId,@PathVariable String status) {
        return ResponseEntity.ok(employeeService.filterFriends(empId, status));
    }

    @GetMapping("/find-friend/{empId}")
    public ResponseEntity<List<FriendCommonDto>> findNewFriends(@PathVariable String empId) {
        return ResponseEntity.ok(employeeService.findNewFriends(empId));
    }


    @GetMapping("/send-req/{empId}/{friendId}")
    public ResponseEntity<Boolean> sendFriendRequest(@PathVariable String empId, @PathVariable String friendId) {
        return ResponseEntity.ok(employeeService.sendFriendRequest(empId, friendId));
    }

    @GetMapping("/friend/react/{friendshipId}/{status}")
    public ResponseEntity<Boolean> acceptRejectFriends(@PathVariable String friendshipId,@PathVariable String status) {
        return ResponseEntity.ok(employeeService.acceptRejectFriendship(friendshipId, status));
    }


//    @PutMapping("/skill/{empId}/{skillId}")
//    public ResponseEntity<Boolean> updateSkill(@PathVariable String empId, @PathVariable String skillId, @RequestBody SkillUtilDto skillUtilDto) {
//        return ResponseEntity.ok(employeeService.updateSkill(empId,skillId, skillUtilDto));
//    }


    @PostMapping("/ability/{empId}")
    public ResponseEntity<Boolean> createAbility(@PathVariable String empId, @RequestBody AbilityDto abilityDto) {
        return ResponseEntity.ok(employeeService.createAbiltiy(empId, abilityDto));
    }

    @PutMapping("/ability/{abilityId}")
    public ResponseEntity<Boolean> updateAbility(@PathVariable String abilityId,
                                                 @RequestBody AbilityDto abilityDto) {
        return ResponseEntity.ok(employeeService.updateAbility(abilityId, abilityDto));
    }

    @DeleteMapping("/ability/{abilityId}")
    public ResponseEntity<Boolean> deleteAbility(@PathVariable String abilityId) {
        return ResponseEntity.ok(employeeService.deleteAbility(abilityId));
    }

    @GetMapping("/ability/{id}")
    public ResponseEntity<AbilityDto> fetchAbilityRecord(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.fetchAbiltiyRecord(id));
    }
}
