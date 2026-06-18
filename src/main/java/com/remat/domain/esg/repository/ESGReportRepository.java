package com.remat.domain.esg.repository;

import com.remat.domain.esg.entity.ESGReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ESGReportRepository extends JpaRepository<ESGReport, Long> {

    Optional<ESGReport> findTopByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId);
}
