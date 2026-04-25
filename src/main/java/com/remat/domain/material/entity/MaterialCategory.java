package com.remat.domain.material.entity;

import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "material_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MaterialCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "esg_effect", nullable = false)
    private Integer esgEffect;
}
