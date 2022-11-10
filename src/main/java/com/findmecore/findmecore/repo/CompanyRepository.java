
package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.CompanyMst;
import com.findmecore.findmecore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface CompanyRepository extends JpaRepository<CompanyMst, Long> {
}
