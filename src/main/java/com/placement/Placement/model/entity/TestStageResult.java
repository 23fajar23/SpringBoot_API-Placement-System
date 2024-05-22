package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EResultTest;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Builder(toBuilder = true)
@Entity
@Table(name = DbPath.TEST_RESULT)
public class TestStageResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    @JsonBackReference
    private Stage stage;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private Application application;

    @Column(name = "result_test")
    @Enumerated(EnumType.STRING)
    private EResultTest result;
}
