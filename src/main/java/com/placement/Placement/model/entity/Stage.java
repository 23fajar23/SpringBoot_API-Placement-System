package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.STAGE)
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EType type;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<Quota> quotas;

}
