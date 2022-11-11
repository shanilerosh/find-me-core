package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Ability;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface PostRepository extends JpaRepository<Post, Long> {

}
