package com.findmecore.findmecore.config;

import com.findmecore.findmecore.dto.SocialProvider;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Role;
import com.findmecore.findmecore.entity.User;
import com.findmecore.findmecore.repo.EmployeeRepository;
import com.findmecore.findmecore.repo.RoleRepository;
import com.findmecore.findmecore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        // Create initial roles
        Role employeeRole = createRoleIfNotFound(Role.ROLE_EMPLOYEE);
        Role employerRole = createRoleIfNotFound(Role.ROLE_EMPLOYEE);
        createRoleIfNotFound(Role.ROLE_EMPLOYEE);
        createRoleIfNotFound(Role.ROLE_EMPLOYER);
//        Employee employee = createEmployee("");
//        createUserIfNotFound("admin@javachinna.com", Set.of(userRole, adminRole, modRole));
//        alreadySetup = true;
    }

    @Transactional
    User createUserIfNotFound(final String email, Set<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setDisplayName("Admin");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("admin@"));
            user.setRoles(roles);
            user.setProvider(SocialProvider.LOCAL.getProviderType());
            user.setEnabled(true);
            Date now = Calendar.getInstance().getTime();
            user.setCreatedDate(now);
            user.setModifiedDate(now);
            user = userRepository.save(user);
        }
        return user;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = roleRepository.save(new Role(name));
        }
        return role;
    }

    @Transactional
    Employee createEmployee(final String name) {
        Optional<Employee> byId = employeeRepository.findById(1L);

        if(byId.isEmpty()) {
            Employee build = Employee.builder().employeeId(1L).address("Negombo")
                    .email("dinesh@gmail.com").name("Dinesh Fernando")
                    .intro("Cool GUY").ref1("1").ref2("RWR").build();
            return employeeRepository.save(build);

        }

        return byId.get();
    }
}
