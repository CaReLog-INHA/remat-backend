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

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "avg_weight_kg", nullable = false)
    private Double avgWeightKg;

    // 자재 1kg당 CO2 배출량 (g CO2 eq/kg). 값이 클수록 재사용/대체 시 감축 효과 큼
    @Column(name = "esg_effect", nullable = false)
    private Integer esgEffect;
}
