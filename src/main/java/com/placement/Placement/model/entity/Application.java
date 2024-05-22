package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EResultTest;
import com.placement.Placement.model.entity.auth.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.APPLICATION)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "datetime", nullable = false)
    private LocalDate dateTime;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test;

    @Column(name = "final_result")
    @Enumerated(EnumType.STRING)
    private EResultTest finalResult;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    List<TestStageResult> testStageResultList;
}
