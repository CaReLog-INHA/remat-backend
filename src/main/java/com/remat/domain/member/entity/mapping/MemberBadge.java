package com.remat.domain.member.entity.mapping;

import com.remat.domain.member.entity.Badge;
import com.remat.domain.member.entity.Member;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "member_badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberBadge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "is_earned", nullable = false)
    private Boolean isEarned;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
