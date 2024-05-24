package com.placement.Placement.model.entity;

import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EMessage;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.auth.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.MESSAGE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer recipient;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "read", nullable = false)
    @Enumerated(EnumType.STRING)
    private EMessage read;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;
}
