package com.remat.domain.ai.entity;

import com.remat.domain.material.entity.Material;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ai_matched_material")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AIMatchedMaterial extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AIAnalysis aiAnalysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_material_id", nullable = false)
    private Material matchedMaterial;
}
