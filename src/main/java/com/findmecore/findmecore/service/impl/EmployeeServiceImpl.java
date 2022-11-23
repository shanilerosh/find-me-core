package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import com.findmecore.findmecore.service.PostService;
import com.findmecore.findmecore.utility.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
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
    private FriendRepository friendRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AbilityRepository abilityRepository;

    @Override
    public Boolean updateEmployeeBasicData(String empId, EmployeeDto employeeDto, MultipartFile file) {

        Employee employee = findEmployeeById(empId);

        employee.setAboutMe(employeeDto.getAboutMe());
        employee.setAddress(employeeDto.getAddress());
        employee.setCity(employeeDto.getCity());
        employee.setCountry(employeeDto.getCountry());
        employee.setMobile(employeeDto.getMobile());
        employee.setName(employeeDto.getName());
        employee.setTown(employeeDto.getTown());

        String path = employee.getProfilePicLocation();

        //update profile pic
        if(null != file) {
            path = saveFile(file);
        }

        employee.setProfilePicLocation(path);

        employeeRepository.save(employee);
        return Boolean.TRUE;
    }

    @Override
    public EmployeeDto findEmployeeByUsername(String empId) {
        Employee employee = findEmployeeById(empId);

        EmployeeDto build = EmployeeDto.builder().build();

        BeanUtils.copyProperties(employee, build);

        List<CourseDto> courseList = getCourseDtoListByEmployee(employee);
        List<ExperienceDto> experienceList = getExperienceListByEmployee(employee);


        //find the current company
        Optional<ExperienceDto> currentExperiece = experienceList.stream().filter(ExperienceDto::isCurrent).findFirst();

        //fetch list of skills
        List<AbilityDto> listOfAbilities = fetchAbilitiesByEmployee(empId, employee);

        //fetch friends connections
        List<FriendDto> listOfFriends = getCurrentFriendListOfAnEmployee(employee);
        List<PostDto> postDtos = postService.fetchPostsByEmployee(employee);
        int connectionSize = connectionRepository.findAllByFollowedEmployee(employee).size();


        build.setCurrentCompany(currentExperiece.isPresent() ? currentExperiece.get().getCompany() : "Unemployed");
        build.setCurrentPostion(currentExperiece.isPresent() ? currentExperiece.get().getPosition() : "Unemployed");

        Optional<CourseDto> currentMajor = courseList.stream().filter(CourseDto::isDisplayMajor).findFirst();

        build.setDisplayInstitute(currentMajor.isPresent() ? currentMajor.get().getInstitute() : "");

        build.setCourseList(courseList);
        build.setExperienceDtos(experienceList);
        build.setAbilityDtos(listOfAbilities);

        build.setListOfFriends(null == listOfFriends ? "0" : Long.toString(listOfFriends.size()));
        build.setListOfPosts(null == postDtos ? "0" : Long.toString(postDtos.size()));
        build.setListOfConnections(connectionSize+"");

        return build;
    }

    private List<AbilityDto> fetchAbilitiesByEmployee(String empId, Employee employee) {
        return abilityRepository
                .findAllByEmployee(employee)
                .stream().map(obj -> AbilityDto.builder().rating(obj.getRating()).skillName(obj.getSkill().getSkillName()).id(obj.getId()).empId(empId).build())
                .collect(Collectors.toList());
    }


    private List<FriendDto> getCurrentFriendListOfAnEmployee(Employee employee) {
        return friendRepository.findAllByCurrentEmployeeAndFriendStatus(employee, FriendStatus.FRIENDS)
                .stream().map(obj -> FriendDto.builder().friendId(obj.getFriend().getEmployeeId()).friendName(obj.getFriend().getName()).friendsSince(getDays(obj.getDateOfFriendShip())).build())
                .collect(Collectors.toList());
    }

    private String getDays(LocalDateTime created) {
        long seconds = ChronoUnit.SECONDS.between(created, LocalDateTime.now());
        if(seconds < 60) {
            return seconds+" seconds ago";
        }
        long mins = ChronoUnit.MINUTES.between(created, LocalDateTime.now());

        if(mins < 60) {
            return mins+" minutes ago";
        }
        long hours = ChronoUnit.HOURS.between(created, LocalDateTime.now());
        if(hours < 24) {
            return hours+" minutes ago";
        }

        long days = ChronoUnit.DAYS.between(created, LocalDateTime.now());

        return days+" days ago";
    }

    private List<ExperienceDto> getExperienceListByEmployee(Employee employee) {
        return employee.getExperiences().stream().map(obj -> ExperienceDto.builder().company(obj.getCompanyMst().getCompanyName())
                .position(obj.getPosition())
                .started(obj.getStarted())
                .isCurrent(obj.isCurrent())
                .ended(obj.getEnded())
                .id(obj.getId())
                .build()).collect(Collectors.toList());
    }

    private List<CourseDto> getCourseDtoListByEmployee(Employee employee) {
        return employee.getCourse().stream().map(obj -> CourseDto.builder().courseName(obj.getCourseName())
                .institute(obj.getEducation().getInstitute())
                .started(obj.getStarted())
                .ended(obj.getEnded())
                .id(obj.getId())
                .isDisplayMajor(obj.isDisplayMajor())
                .build()).collect(Collectors.toList());
    }

    private Employee findEmployeeById(String empId) {
        return employeeRepository.findById(Long.valueOf(empId))
                .orElseThrow(() -> {
                    throw new ResolutionException("Employee not found");
                });
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

    /**
     * Method to save the file to specific path
     * @param file
     * @return
     */
    private String saveFile(MultipartFile file) {
        try {
            File path = new File(StringUtils.IMAGE_SAVE_LOCATION_POST + file.getOriginalFilename());
            boolean newFile = path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(file.getBytes());
            output.close();
            return StringUtils.FRONT_DESTINATION.concat(Objects.requireNonNull(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
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

    @Override
    public Boolean createEmployeeWithUserData(UserDto userDto) {

        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> {
                    throw new UserAlreadyExistAuthenticationException("User not found");
                });

        Employee employee = Employee.builder().email(user.getEmail()).user(user).isUpdatedForTheFirstTime(false)
                .build();

        employeeRepository.save(employee);
        return Boolean.TRUE;
    }

    @Override
    public String generateCv(String empId, CvUtilDto cvUtilDto) {
        int topHeadingFont = 35;
        int mediumFont = 15;
        int smallFont = 8;

//        EmployeeDto employee = findEmployeeByUsername(empId);
//
//        try (PDDocument doc = new PDDocument()) {
//            PDPage pdPage = new PDPage();
//            doc.addPage(pdPage);
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(doc, pdPage)) {
//
//
//                List<Ability> employeeSkills = abilityRepository.findAllByEmployee(employee);
//
//
//                //left colored section
//                contentStream.setNonStrokingColor(Color.DARK_GRAY);
//                contentStream.addRect(0, 0, 200, 900);
//                contentStream.fill();
//
//                contentStream.beginText();
//
//
//                //Side Bar Text Generation
//                contentStream.setNonStrokingColor(Color.WHITE);
//                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
//                contentStream.setLeading(14.5f);
//                contentStream.newLineAtOffset(90, 700);
//
//                contentStream.showText(employee.getName());
//
//                contentStream.newLine();
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
//                contentStream.showText("Contact Me At");
//
//                contentStream.newLine();
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
//                contentStream.setLeading(25f);
//
//                //personal Data
//                contentStream.showText(employee.getMobile());
//                contentStream.newLine();
//                contentStream.showText(employee.getAddress());
//                contentStream.newLine();
//                contentStream.showText(employee.getAddress());
//                contentStream.newLine();
//                contentStream.showText(employee.getEmail());
//                contentStream.newLine();
//                contentStream.showText(employee.getLinkedinProfile());
//
//                //skills section
//                contentStream.newLine();
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
//                contentStream.showText("Skills");
//
//                contentStream.endText();
//
//                //Generate Skills Sections
////
//                PDImageXObject pdImage = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\C#\\star.png",doc);
//
//                int originalStartInitX = 40;
//                int originalStartInitY = 450;
//
//                int starInitX = originalStartInitX;
//                int starInitY = originalStartInitY;
//                int horizontalIncrementor = 15;
//                int startSpace = 15;
//
//                for (Ability employeeSkill : employeeSkills) {
//                    //Side Bar Text Generation
//                    for (int i = 0; i < employeeSkill.getRating(); i++) {
//                        contentStream.drawImage(pdImage,starInitX, starInitY,10,10);
//                        starInitX += startSpace;
//                    }
//                    starInitX = originalStartInitX;
//                    starInitY-=horizontalIncrementor;
//                }
//
//                //Skill title set
//                int skillIncrementorX = originalStartInitX+60;
//                int skillIncrementorY = originalStartInitY;
//
//                contentStream.beginText();
//
//                contentStream.setNonStrokingColor(Color.WHITE);
//                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
//                contentStream.newLineAtOffset(skillIncrementorX, skillIncrementorY);
//                contentStream.setLeading(horizontalIncrementor);
//
//                for (Ability employeeSkill : employeeSkills) {
//                    contentStream.showText(employeeSkill.getSkill().getSkillName());
//                    contentStream.newLine();
//                    skillIncrementorY-=horizontalIncrementor;
//                }
//
//                contentStream.newLine();
//                contentStream.newLine();
//
//                contentStream.moveTextPositionByAmount(-60, 0);
//
//
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
//                contentStream.showText("Non Related Refrees");
//
//                contentStream.newLine();
//                contentStream.newLine();
//
//
//                contentStream.showText(employee.getRef1());
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
//                contentStream.showText(employee.getRef1Position());
//                contentStream.newLine();
//                contentStream.showText(employee.getRef1Address());
//                contentStream.newLine();
//                contentStream.showText(employee.getRef1Mobile());
//
//                contentStream.newLine();
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
//                contentStream.showText(employee.getRef1());
//                contentStream.newLine();
//
//                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
//                contentStream.showText(employee.getRef1Position());
//                contentStream.newLine();
//                contentStream.showText(employee.getRef1Address());
//                contentStream.newLine();
//                contentStream.showText(employee.getRef1Mobile());
//
//                contentStream.endText();
//
//                //Side Bar End
//
//
//                //Profile Section
//                contentStream.beginText();
//                int startingProfileCordinateX = 220;
//                int startingProfileCordinateY = 720;
//
//
//                contentStream.setNonStrokingColor(Color.GRAY);
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 35);
//                contentStream.setLeading(35f);
//                contentStream.newLineAtOffset(startingProfileCordinateX, startingProfileCordinateY);
//
//                //Main Heading Name
//                contentStream.showText("Shanil Miranda");
//
//                contentStream.newLine();
//
//                contentStream.setNonStrokingColor(Color.GRAY);
//                contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
//                contentStream.showText("Personal Profile");
//
//                contentStream.newLine();
//
//                contentStream.setNonStrokingColor(Color.BLACK);
//                contentStream.setFont(PDType1Font.COURIER, smallFont);
//                contentStream.showText("I am hard working, methodical person who bears a good moral character. I can carry out the duties entrusted to me efficiently without any supervision.");
//
//
//
//                contentStream.endText();
//
//            }
//
//            doc.save("C:\\Users\\Admin\\Desktop\\C#\\doc.pdf");
//
//        } catch (IOException ioException) {
//            System.out.println("Handling IOException...");
//        }
        return "";
    }

    @Override
    public List<SkillDto> generateSkills() {
        return skillRepository.findAll().stream().map(obj -> SkillDto.builder()
                .skillName(obj.getSkillName()).id(obj.getId()).build()).collect(Collectors.toList());

    }

    @Override
    public Boolean updateSkill(String empId, String skillId, SkillUtilDto skillUtilDto) {

    return null;

    }

    @Override
    public Boolean createSkill(String empId, SkillUtilDto skillUtilDto) {

        Employee employee = findEmployeeById(empId);
        Skill skill = findSkillById(skillUtilDto.getSkillId());
        isSkillAlreadyExist(employee, skill);

        Ability ability = Ability.builder().skill(skill)
                .rating(skillUtilDto.getRating()).build();

        abilityRepository.save(ability);
        return Boolean.TRUE;
    }

    private void isSkillAlreadyExist(Employee employee, Skill skill) {
        abilityRepository.findAllByEmployee(employee)
                .stream().filter(obj -> obj.getSkill().getId().equals(skill.getId())).findAny().ifPresent(c-> {
            throw new RuntimeException("Skill already exist please try again");
        });
    }

    private Skill findSkillById(long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("Skill not found");
                });
    }
}
