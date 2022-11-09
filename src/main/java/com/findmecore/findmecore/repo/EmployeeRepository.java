package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Ability;
import com.findmecore.findmecore.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
