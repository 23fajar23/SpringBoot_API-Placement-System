package com.placement.Placement.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.QUOTA_BATCH)
public class QuotaBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "available", nullable = false)
    private Integer available;

    @ManyToOne
    @JoinColumn(name = "quota_id")
    @JsonBackReference
    private Quota quota;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonBackReference
    private Batch batch;
}
