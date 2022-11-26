package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Ability;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface AbilityRepository extends JpaRepository<Ability, Long> {

    List<Ability> findAllByEmployee(Employee employee);

    Optional<Ability> findByEmployeeAndSkill(Employee employee, Skill skill);
}
