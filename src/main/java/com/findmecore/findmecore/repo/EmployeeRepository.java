package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Ability;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUser(User user);
}
