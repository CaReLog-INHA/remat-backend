package com.remat.domain.esg.repository;

import com.remat.domain.esg.entity.ESGReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESGReportRepository extends JpaRepository<ESGReport, Long> {
}
