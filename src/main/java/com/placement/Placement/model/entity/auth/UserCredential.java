package com.placement.Placement.model.entity.auth;

import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = DbPath.USER_CREDENTIAL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email", unique = true ,nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}