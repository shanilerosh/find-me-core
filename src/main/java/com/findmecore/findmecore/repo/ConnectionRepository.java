package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.dto.FriendStatus;
import com.findmecore.findmecore.entity.Connection;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    List<Connection> findAllByFollowedEmployee(Employee employee);

}
