package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.entity.NotificationMst;
import com.findmecore.findmecore.entity.Post;
import com.findmecore.findmecore.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface NotificationRepository extends JpaRepository<NotificationMst, Long> {

    List<NotificationMst> findAllByReceiverIdAndParty(Long val, String party);


}
