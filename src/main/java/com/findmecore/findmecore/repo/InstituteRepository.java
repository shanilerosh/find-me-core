
package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.CompanyMst;
import com.findmecore.findmecore.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface InstituteRepository extends JpaRepository<Education, Long> {
}
