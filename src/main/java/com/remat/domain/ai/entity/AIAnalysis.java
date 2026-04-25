package com.remat.domain.ai.entity;

import com.remat.domain.member.entity.Member;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ai_analysis")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AIAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "file_url", columnDefinition = "TEXT", nullable = false)
    private String fileUrl;

    @Column(name = "matched_count", nullable = false)
    private Integer matchedCount;

    @Column(name = "analysis_result", columnDefinition = "JSON", nullable = false)
    private String analysisResult;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "aiAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AIMatchedMaterial> aiMatchedMaterials = new ArrayList<>();

    @OneToMany(mappedBy = "aiAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AIRequiredMaterial> aiRequiredMaterials = new ArrayList<>();

    public void addMatchedMaterial(AIMatchedMaterial matchedMaterial){
        aiMatchedMaterials.add(matchedMaterial);
        matchedMaterial.setAiAnalysis(this);
    }

    public void addRequiredMaterial(AIRequiredMaterial requiredMaterial){
        aiRequiredMaterials.add(requiredMaterial);
        requiredMaterial.setAiAnalysis(this);
    }
}
