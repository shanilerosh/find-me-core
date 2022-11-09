package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Skill;
import com.findmecore.findmecore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {

}
