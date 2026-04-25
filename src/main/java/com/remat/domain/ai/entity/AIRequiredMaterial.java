package com.remat.domain.ai.entity;

import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ai_required_material")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AIRequiredMaterial extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AIAnalysis aiAnalysis;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "description", nullable = false)
    private String description;

    public void setAiAnalysis(AIAnalysis aiAnalysis){
        this.aiAnalysis = aiAnalysis;
    }
}
