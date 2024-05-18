package com.placement.Placement.model.entity.auth;

import com.placement.Placement.constant.DbPath;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Education;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DbPath.CUSTOMER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Column(name = "address", nullable = true, length = 50)
    private String address;

    @Column(name = "phone", nullable = true, unique = true ,length = 50)
    private String mobilePhone;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "education_id")
    private Education education;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

}
