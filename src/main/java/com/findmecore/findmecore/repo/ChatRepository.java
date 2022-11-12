
package com.findmecore.findmecore.repo;

import com.findmecore.findmecore.entity.ChatMst;
import com.findmecore.findmecore.entity.CompanyMst;
import com.findmecore.findmecore.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface ChatRepository extends JpaRepository<ChatMst, Long> {

    List<ChatMst> findAllBySendingEmployeeAndReceivingEmployeeId(Employee sendingEmp, Employee received);

    List<ChatMst> findAllBySendingEmployeeOrReceivingEmployeeId(Employee employee, Employee rec);
}
