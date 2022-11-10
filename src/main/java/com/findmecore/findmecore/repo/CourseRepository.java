
package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.CompanyMst;
import com.findmecore.findmecore.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
}
