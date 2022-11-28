package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface JobEmployeeRepository extends JpaRepository<JobEmployee, Long> {

    Optional<JobEmployee> findByEmployeeAndJob(Employee employee, Job job);

    List<JobEmployee> findAllByJob(Job job);
}
