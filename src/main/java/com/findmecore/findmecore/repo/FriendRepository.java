package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.dto.FriendStatus;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.Friend;
import com.findmecore.findmecore.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllByCurrentEmployeeAndFriendStatus(Employee employee, FriendStatus status);

    List<Friend> findAllByFriendAndFriendStatus(Employee employee, FriendStatus status);

}
