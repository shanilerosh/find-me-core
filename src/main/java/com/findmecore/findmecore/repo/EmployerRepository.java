package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Employer;
import com.findmecore.findmecore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Optional<Employer> findByUser(User user);
}
