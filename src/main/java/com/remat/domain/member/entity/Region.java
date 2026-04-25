package com.remat.domain.member.entity;

import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "region")
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_name", nullable = false)
    private String regionName;
}
