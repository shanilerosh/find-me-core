
package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Role;
import com.findmecore.findmecore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ShanilErosh
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
