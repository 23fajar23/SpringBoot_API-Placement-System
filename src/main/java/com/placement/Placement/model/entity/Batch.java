package com.placement.Placement.model.entity;

import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.ERegion;
import com.placement.Placement.constant.EStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@Table(name = DbPath.BATCH)
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERegion region;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;
}
