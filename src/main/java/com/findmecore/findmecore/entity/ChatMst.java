package com.findmecore.findmecore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sending_employee_id")
    private Employee sendingEmployee;

    @ManyToOne
    @JoinColumn(name = "receiving_employee_id")
    private Employee receivingEmployeeId;

    private LocalDateTime messageTime;

    private String chatContent;

}
