package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Post;
import com.findmecore.findmecore.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface ReactRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByPostAndEmployee(Post post, Employee employee);

}
