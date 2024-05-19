package com.placement.Placement.model.entity;

import com.placement.Placement.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = DbPath.EDUCATION)
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "education", nullable = false, unique = true)
    private String education;

    @Column(name = "value", nullable = false)
    private int value;
}
