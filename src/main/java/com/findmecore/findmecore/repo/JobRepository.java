package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findAllByEmployerAndStatus(Employer employer, String status);

    List<Job> findAllByStatus(String status);

}
