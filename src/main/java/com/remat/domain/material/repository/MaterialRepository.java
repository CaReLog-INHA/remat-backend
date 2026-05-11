package com.remat.domain.material.repository;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.MaterialCategory;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.member.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT m FROM Material m WHERE m.deletedAt IS NULL " +
            "AND (:category IS NULL OR m.category = :category) " +
            "AND (:materialCondition IS NULL OR m.materialCondition = :materialCondition) " +
            "AND (:transactionType IS NULL OR m.transactionType = :transactionType) " +
            "AND (:region IS NULL OR m.region = :region) " +
            "ORDER BY m.createdAt DESC")
    List<Material> findAllWithFilters(
            @Param("category") MaterialCategory category,
            @Param("materialCondition") MaterialCondition materialCondition,
            @Param("transactionType") TransactionType transactionType,
            @Param("region") Region region
    );
}
