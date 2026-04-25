package com.remat.domain.member.entity;

import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Badge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "badge_name", nullable = false)
    private String badgeName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;
}
