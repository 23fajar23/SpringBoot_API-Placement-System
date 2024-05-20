package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EQuota;
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
@Table(name = DbPath.QUOTA)
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "available", nullable = false)
    private int available;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    @JsonBackReference
    private Stage stage;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EQuota type;

    @OneToMany(mappedBy = "quota", cascade = CascadeType.ALL)
    private List<QuotaBatch> quotaBatches;
}
