package com.placement.Placement.model.entity;

import com.placement.Placement.constant.DbPath;
import com.placement.Placement.constant.EQuota;
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
@Table(name = DbPath.QUOTA)
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "total", nullable = false)
    private String total;

    @Column(name = "available", nullable = false)
    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EQuota type;
}
