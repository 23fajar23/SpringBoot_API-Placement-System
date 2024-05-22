package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.TEST)
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "placement", nullable = false)
    private String placement;

    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "role_placement", nullable = false)
    private String rolePlacement;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    @ManyToOne
    @JoinColumn(name = "education_id")
    @JsonBackReference
    private Education education;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Stage> stages;

}
