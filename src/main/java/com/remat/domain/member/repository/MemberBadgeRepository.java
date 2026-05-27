package com.remat.domain.member.repository;

import com.remat.domain.member.entity.mapping.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    @Query("SELECT mb FROM MemberBadge mb " +
            "JOIN FETCH mb.badge " +
            "WHERE mb.member.id = :memberId " +
            "AND mb.deletedAt IS NULL " +
            "ORDER BY mb.createdAt DESC")
    List<MemberBadge> findAllByMemberIdWithBadge(@Param("memberId") Long memberId);

    @Query("SELECT mb FROM MemberBadge mb " +
            "JOIN FETCH mb.badge " +
            "WHERE mb.member.id = :memberId " +
            "AND mb.isEarned = true " +
            "AND mb.deletedAt IS NULL " +
            "ORDER BY mb.createdAt DESC")
    List<MemberBadge> findEarnedByMemberIdWithBadge(@Param("memberId") Long memberId);
}
