package com.findmecore.findmecore.service.impl;

import com.findmecore.findmecore.dto.*;
import com.findmecore.findmecore.entity.*;
import com.findmecore.findmecore.exceptions.BadRequestException;
import com.findmecore.findmecore.exceptions.UserAlreadyExistAuthenticationException;
import com.findmecore.findmecore.repo.*;
import com.findmecore.findmecore.service.EmployeeService;
import com.findmecore.findmecore.service.NotificationService;
import com.findmecore.findmecore.service.PostService;
import com.findmecore.findmecore.utility.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ShanilErosh
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {


    private  final PDFont FONT = PDType1Font.COURIER;
    private  final float FONT_SIZE =8;
    private  final float LEADING = -1.5f * FONT_SIZE;

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

    @Autowired
    private NotificationService notificationService;

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
                .stream().map(obj -> AbilityDto.builder().rating(obj.getRating()).skillName(obj.getSkill().getSkillName()).id(obj.getId()).empId(empId)
                        .skillId(obj.getSkill().getId()).build())
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

        checkIfDateIsBelow(expUtil.getStartDate(), expUtil.getEndDate());

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

        checkIfDateIsBelow(expUtil.getStarted(), expUtil.getEnded());

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

        //validate days
        checkIfDateIsBelow(expUtil.getStartDate(), expUtil.getEndDate());

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

        checkIfDateIsBelow(expUtil.getStarted(), expUtil.getEnded());

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

        Employee savedEmploee = employeeRepository.save(employee);

        user.setEmployee(savedEmploee);
        userRepository.save(user);

        return Boolean.TRUE;
    }

    @Override
    public String generateCv(String empId, CvUtilDto cvUtilDto) throws IOException {
        int topHeadingFont = 35;
        int mediumFont = 15;
        int mediumTitleFont = 11;
        int smallFont = 8;

         float LEADING = -1.5f * mediumFont;



//        EmployeeDto employee = findEmployeeByUsername(empId);
        Employee employee = findEmployeeById(empId);

        PDDocument doc = new PDDocument();
        PDPage pdPage = new PDPage();
        doc.addPage(pdPage);
        PDPageContentStream contentStream = new PDPageContentStream(doc, pdPage);


        List<Ability> employeeSkills = abilityRepository.findAllByEmployee(employee);


        //left colored section
        contentStream.setNonStrokingColor(Color.DARK_GRAY);
        contentStream.addRect(0, 0, 200, 900);
        contentStream.fill();


        //set profile picture
        PDImageXObject profileImg = PDImageXObject.createFromFile(StringUtils.IMAGE_SAVE_LOCATION_PDF_UTIL+employee.getProfilePicLocation(),doc);
        contentStream.drawImage(profileImg,20, 680,100,100);


        contentStream.beginText();

        //Side Bar Text Generation
        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(30, 650);

        contentStream.showText(handleEmpty(employee.getName()));

        contentStream.newLine();
        contentStream.newLine();

        contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
        contentStream.showText("Contact Me At");

        contentStream.newLine();
        contentStream.newLine();

        contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
        contentStream.setLeading(25f);

        //personal Data
        contentStream.showText(handleEmpty(employee.getMobile()));
        contentStream.newLine();
        contentStream.showText(handleEmpty(employee.getAddress()));
        contentStream.newLine();
        contentStream.showText(handleEmpty(employee.getAddress()));
        contentStream.newLine();
        contentStream.showText(handleEmpty(employee.getEmail()));
        contentStream.newLine();
        contentStream.showText(handleEmpty(employee.getLinkedinProfile()));

                //skills section
                contentStream.newLine();
                contentStream.newLine();

                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
                contentStream.showText("Skills");

                contentStream.endText();

                //Generate Skills Sections
//
                PDImageXObject pdImage = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\C#\\star.png",doc);

                PDImageXObject phoneImage = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\C#\\phone.png",doc);
                PDImageXObject homeImg = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\C#\\home.png",doc);

                //x = 15 y= 621
                contentStream.drawImage(phoneImage, 10, 590,11,11);
                contentStream.drawImage(homeImg, 10, 590-25,11,11);
        contentStream.drawImage(homeImg, 10, 590-25,11,11);


                int originalStartInitX = 20;
                int originalStartInitY = 410;

                int starInitX = originalStartInitX;
                int starInitY = originalStartInitY;
                int horizontalIncrementor = 15;
                int startSpace = 15;

                for (Ability employeeSkill : employeeSkills) {
                    //Side Bar Text Generation
                    for (int i = 0; i < employeeSkill.getRating(); i++) {
                        contentStream.drawImage(pdImage,starInitX, starInitY,5,5);
                        starInitX += startSpace;
                    }
                    starInitX = originalStartInitX;
                    starInitY-=horizontalIncrementor;
                }

                //Skill title set
                int skillIncrementorX = originalStartInitX+75;
                int skillIncrementorY = originalStartInitY;

                contentStream.beginText();

                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.newLineAtOffset(skillIncrementorX, skillIncrementorY);
                contentStream.setLeading(horizontalIncrementor);

                for (Ability employeeSkill : employeeSkills) {
                    contentStream.showText(handleEmpty(employeeSkill.getSkill().getSkillName()));
                    contentStream.newLine();
                    skillIncrementorY-=horizontalIncrementor;
                }

                contentStream.newLine();
                contentStream.newLine();

                contentStream.moveTextPositionByAmount(-60, 0);


                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
                contentStream.showText("Non Related Refrees");

                contentStream.newLine();
                contentStream.newLine();


                contentStream.showText(handleEmpty(employee.getRef1()));
                contentStream.newLine();

                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.showText(handleEmpty(employee.getRef1Position()));
                contentStream.newLine();
                contentStream.showText(handleEmpty(employee.getRef1Address()));
                contentStream.newLine();
                contentStream.showText(handleEmpty(employee.getRef1Mobile()));

                contentStream.newLine();
                contentStream.newLine();

                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
                contentStream.showText(handleEmpty(employee.getRef1()));
                contentStream.newLine();

                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.showText(handleEmpty(employee.getRef1Position()));
                contentStream.newLine();
                contentStream.showText(handleEmpty(employee.getRef1Address()));
                contentStream.newLine();
                contentStream.showText(handleEmpty(employee.getRef1Mobile()));

                contentStream.endText();

                //Side Bar End


                //Profile Section
                contentStream.beginText();
                int startingProfileCordinateX = 220;
                int startingProfileCordinateY = 720;


                contentStream.setNonStrokingColor(Color.GRAY);
                contentStream.setFont(PDType1Font.TIMES_BOLD, 35);
                contentStream.setLeading(35f);
                contentStream.newLineAtOffset(startingProfileCordinateX, startingProfileCordinateY);

                //Main Heading Name
                contentStream.showText("Shanil Miranda");

                contentStream.newLine();

                contentStream.setNonStrokingColor(Color.GRAY);
                contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
                contentStream.showText("Personal Profile");

                contentStream.newLine();

                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.COURIER, smallFont);


                contentStream.newLineAtOffset(3, 15);

                addParagraph(contentStream,380,"Ut sodales arcu molestie, auctor ex rhoncus, consequat turpis. Donec vehicula suscipit vehicula. Vestibulum euismod sed eros ac accumsan. Aliquam ornare imperdiet dignissim. Suspendisse suscipit velit dapibus risus aliquam, ornare aliquet lorem placerat. Quisque est metus, blandit eu magna tempus, aliquam cursus est. Quisque varius nisi id lectus sodales facilisis. Donec placerat diam id quam bibendum auctor. Quisque dapibus odio at justo interdum pellentesque. Integer at risus ut lorem efficitur fermentum posuere tincidunt ex.");

                contentStream.setLeading(20f);
                contentStream.newLine();

        contentStream.setNonStrokingColor(Color.GRAY);
        contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
        contentStream.showText("Education");

        contentStream.newLine();


        for (Course course : employee.getCourse()) {
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.setFont(PDType1Font.TIMES_BOLD, mediumTitleFont);

            contentStream.showText(course.getEducation().getInstitute());
            contentStream.setLeading(10f);

            contentStream.newLine();

            //Fixed-Dont Change this
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, smallFont);


            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();


            start.setTime(course.getStarted());
            end.setTime(course.getEnded());
            contentStream.showText(course.getCourseName()+" | "+start.get(Calendar.YEAR) + " - " +
                    end.get(Calendar.YEAR));

            contentStream.setLeading(20f);
            contentStream.newLine();
        }

        contentStream.setLeading(25f);
        contentStream.newLine();


        contentStream.setNonStrokingColor(Color.GRAY);
        contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
        contentStream.showText("Experience");

        contentStream.newLine();


        for (Experience experience : employee.getExperiences()) {
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.setFont(PDType1Font.TIMES_BOLD, mediumTitleFont);

            contentStream.showText(experience.getCompanyMst().getCompanyName());
            contentStream.setLeading(10f);

            contentStream.newLine();

            //Fixed-Dont Change this
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, smallFont);


            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();


            start.setTime(experience.getStarted());
            end.setTime(experience.getEnded());
            contentStream.showText(experience.getPosition()+" | "+start.get(Calendar.YEAR) + " - " +
                    end.get(Calendar.YEAR));

            contentStream.setLeading(20f);
            contentStream.newLine();
        }

        contentStream.setLeading(25f);
        contentStream.newLine();


        contentStream.setNonStrokingColor(Color.GRAY);
        contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
        contentStream.showText("Other Remarks");


        contentStream.newLine();

        addParagraph(contentStream,380,"I am hard working, methodical person who bears a good moral character. I can carry out the duties entrusted to me efficiently without any supervision.");

        contentStream.setLeading(25f);
        contentStream.newLine();


        contentStream.setNonStrokingColor(Color.GRAY);
        contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
        contentStream.showText("Extracurricular Activities");


        contentStream.newLine();

        addParagraph(contentStream,380,"I am hard working, methodical person who bears a good moral character. I can carry out the duties entrusted to me efficiently without any supervision.");






            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            doc.save(stream);
            doc.close();

        return Base64.getEncoder().encodeToString(stream.toByteArray());
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

    @Override
    public List<FriendCommonDto> filterFriends(String empId, String status) {

        Employee employeeById = findEmployeeById(empId);

        FriendStatus friendStatus = FriendStatus.valueOf(status);
        return findFriendsByEmployeeAndStatus(employeeById, friendStatus);
    }

    @Override
    public Boolean acceptRejectFriendship(String friendshipId, String status) {

        Friend friend = findFriendByFriendshipId(friendshipId);

        friend.setFriendStatus(FriendStatus.valueOf(status));
        Friend save = friendRepository.save(friend);


        NotificationDto notificationDto = NotificationDto.builder().shooterProfilePic(friend.getCurrentEmployee().getProfilePicLocation())
                .shooterProfileName(friend.getCurrentEmployee().getName())
                .creatorId(friend.getId())
                .receiverId(friend.getFriend().getEmployeeId())
                .type("non-msg")
                .party(Role.ROLE_EMPLOYEE)
                .isRead(false).message(friend.getCurrentEmployee().getName() + ((status.equals(FriendStatus.FRIENDS.toString()) ?
                        " accepted " : " rejected ") + "your friend request"))
                .isPositive(status.equals(FriendStatus.FRIENDS.toString())).build();

        notificationService.createNotification(notificationDto);

        return true;
    }

    private Friend findFriendByFriendshipId(String friendshipId) {
        return friendRepository.findById(Long.valueOf(friendshipId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Friendship not found");
                });
    }

    @Override
    public List<FriendCommonDto> findFriendsByEmployeeAndStatus(Employee employeeById, FriendStatus status) {
        List<FriendCommonDto> friendList = friendRepository.findAllByCurrentEmployeeAndFriendStatus(employeeById, status)
                .stream().map(this::getFriendCommonDto).collect(Collectors.toList());

        //f both are friends should be taken from other side
        if(status == FriendStatus.FRIENDS) {

            List<FriendCommonDto> otherFriendSet = friendRepository.findAllByFriendAndFriendStatus(employeeById, status)
                    .stream().map(obj -> getFriendCommonDto(obj, false)).collect(Collectors.toList());

            LinkedHashSet<FriendCommonDto> hashSet = new LinkedHashSet<>(friendList);
            hashSet.addAll(otherFriendSet);

            return new ArrayList<>(hashSet);
        }
        return friendList;
    }

    @Override
    public Boolean createAbiltiy(String empId, AbilityDto abilityDto) {
        Employee employeeById = findEmployeeById(empId);
        Skill skill = findSkillById(abilityDto.getSkillId());

        Optional<Ability> isDuplicated = abilityRepository.findByEmployeeAndSkill(employeeById, skill);

        Ability ability;

        if(isDuplicated.isPresent()) {
            ability = isDuplicated.get();
            ability.setRating(abilityDto.getRating());
        }else{
            ability = Ability.builder().employee(employeeById)
                    .skill(skill).rating(abilityDto.getRating())
                    .build();
        }
        abilityRepository.save(ability);
        return true;
    }

    @Override
    public Boolean updateAbility(String abilityId, AbilityDto abilityDto) {

        Ability ability = findABilityById(abilityId);

        Skill skill = findSkillById(abilityDto.getSkillId());

        ability.setSkill(skill);
        ability.setRating(abilityDto.getRating());

        abilityRepository.save(ability);
        return true;
    }

    @Override
    public Boolean deleteAbility(String abilityId) {

        Ability ability = findABilityById(abilityId);
        abilityRepository.delete(ability);

        return true;
    }

    @Override
    public AbilityDto fetchAbiltiyRecord(String id) {

        Ability ability = findABilityById(id);
        return converAbilityEntitytoDto(ability);
    }

    @Override
    public Employee findEmployeeByUser(User id) {
        return employeeRepository.findByUser(id)
                .orElseThrow(()-> {
                    throw new RuntimeException("user not found");
                });
    }

    @Override
    public List<FriendCommonDto> findNewFriends(String empId) {

        Employee employee = findEmployeeById(empId);

        List<FriendCommonDto> friends = findFriendsByEmployeeAndStatus(employee, FriendStatus.FRIENDS);
        List<FriendCommonDto> requested = findFriendsByEmployeeAndStatus(employee, FriendStatus.REQUESTED);

        //fetch my requested list
        List<Friend> myRequestedList = friendRepository
                .findAllByFriendAndFriendStatus(employee, FriendStatus.REQUESTED);


        return employeeRepository.findAll().stream().filter(obj -> !obj.getEmployeeId().equals(employee.getEmployeeId())
                && friends.stream().noneMatch(friend -> obj.getEmployeeId().equals(friend.getFriendEmpId()))
                && myRequestedList.stream().noneMatch(friend -> obj.getEmployeeId().equals(friend.getCurrentEmployee().getEmployeeId()))
                && requested.stream().noneMatch(reqFriend -> obj.getEmployeeId().equals(reqFriend.getFriendEmpId()))).

                map(this::getFriendCommonDto).
                distinct().collect(Collectors.toList());
    }

    @Override
    public Boolean sendFriendRequest(String empId, String friendId) {

        Employee requestingEmpl = findEmployeeById(empId);
        Employee receivingEmpl = findEmployeeById(friendId);

        friendRepository.findByCurrentEmployeeAndFriend(receivingEmpl, requestingEmpl)
            .ifPresent(c -> {
                throw new RuntimeException("Friendship already exist");
            });

        Friend build = Friend.builder().friend(requestingEmpl).currentEmployee(receivingEmpl).dateOfFriendShip(LocalDateTime.now())
                .friendStatus(FriendStatus.REQUESTED).build();

        friendRepository.save(build);

        return true;
    }

    private AbilityDto converAbilityEntitytoDto(Ability ability) {
        return AbilityDto.builder()
                .skillId(ability.getSkill().getId()).skillName(ability.getSkill().getSkillName())
                .rating(ability.getRating()).id(ability.getId()).build();
    }

    /**
     * Find an employee by id
     * @param abilityId
     * @return
     */
    private Ability findABilityById(String abilityId) {
        return abilityRepository.findById(Long.valueOf(abilityId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Ability not found");
                });
    }

    /**
     * Find a skill by id
     * @param skillId
     * @return
     */
    private Skill findSkillById(String skillId) {
        return skillRepository.findById(Long.valueOf(skillId))
                .orElseThrow(() -> {
                    throw new RuntimeException("Skill not found");
                });
    }

    private void addParagraph(PDPageContentStream contentStream, float width,String text) throws IOException {
        addParagraph(contentStream, width, text, false);
    }

    private void addParagraph(PDPageContentStream contentStream, float width, String text, boolean justify) throws IOException {
        List<String> lines = parseLines(text, width);
        contentStream.setFont(PDType1Font.COURIER, 8);
        for (String line: lines) {
            float charSpacing = 0;
            if (justify){
                if (line.length() > 1) {
                    float size = 8 * PDType1Font.COURIER.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            //contentStream.s(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, LEADING);
        }
    }

    private List<String> parseLines(String text, float width) throws IOException {
        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            String subString = text.substring(0, spaceIndex);
            float size = FONT_SIZE * FONT.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0){
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }


    /**
     * Create freind commonddto by a friend enttiy obj
     * @param obj
     * @return
     */
    private FriendCommonDto getFriendCommonDto(Friend obj) {
        Employee friendProfile = obj.getFriend();
        return FriendCommonDto.builder()
                .friendId(obj.getId()).friendEmpId(friendProfile.getEmployeeId()).
                        friendInfo(friendProfile.getAboutMe()).isFriend(obj.getFriendStatus().equals(FriendStatus.FRIENDS))
                .friendPhoto(friendProfile.getProfilePicLocation())
                .friendName(friendProfile.getName())
                .friendEmail(friendProfile.getEmail())
                .build();
    }

    private FriendCommonDto getFriendCommonDto(Employee obj) {
        return FriendCommonDto.builder()
                .friendId(null).friendEmpId(obj.getEmployeeId()).
                        friendInfo(obj.getAboutMe()).isFriend(false)
                .friendPhoto(obj.getProfilePicLocation())
                .friendName(obj.getName())
                .friendEmail(obj.getEmail())
                .build();
    }

    private FriendCommonDto getFriendCommonDto(Friend obj, boolean flag) {
        Employee friendProfile = obj.getCurrentEmployee();
        return FriendCommonDto.builder()
                .friendId(obj.getId()).friendEmpId(friendProfile.getEmployeeId()).
                        friendInfo(friendProfile.getAboutMe()).isFriend(obj.getFriendStatus().equals(FriendStatus.FRIENDS))
                .friendPhoto(friendProfile.getProfilePicLocation())
                .friendName(friendProfile.getName())
                .friendEmail(friendProfile.getEmail())
                .build();
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

    private void checkIfDateIsBelow(Date start, Date end) {
        if(start.after(end)) {
            throw new RuntimeException("Your start date cannot be before your end date");
        }
    }

    private String handleEmpty(String data) {
        return data == null ? "-" : data;
    }
}
