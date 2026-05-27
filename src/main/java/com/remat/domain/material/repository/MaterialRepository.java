package com.remat.domain.material.repository;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.MaterialCategory;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByIdAndDeletedAtIsNull(Long id);

    List<Material> findAllByMemberAndDeletedAtIsNullOrderByCreatedAtDesc(Member member);

    List<Material> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId);
  
    // 코사인 거리 기준 유사 자재 조회 (embedding::vector 캐스팅 사용)
    @Query(value = "SELECT id, (embedding::vector <=> CAST(:queryVector AS vector)) AS distance " +
                   "FROM material WHERE deleted_at IS NULL AND embedding IS NOT NULL " +
                   "ORDER BY distance LIMIT :limit",
           nativeQuery = true)
    List<SimilarityProjection> findSimilarMaterials(
            @Param("queryVector") String queryVector,
            @Param("limit") int limit
    );

    interface SimilarityProjection {
        Long getId();
        Double getDistance(); // 코사인 거리 (0 = 완전 동일, 1 = 완전 반대)
    }

    @Query("SELECT m FROM Material m WHERE m.id IN :ids AND m.deletedAt IS NULL")
    List<Material> findAllByIdIn(@Param("ids") Set<Long> ids);

    @Query("SELECT m FROM Material m WHERE m.embedding IS NULL AND m.deletedAt IS NULL")
    List<Material> findAllWithNullEmbedding();

    @Query("SELECT m FROM Material m WHERE m.deletedAt IS NULL " +
            "AND (:keyword IS NULL " +
            "OR LOWER(m.materialName) LIKE CONCAT('%', :keyword, '%') " +
            "OR LOWER(m.description) LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:category IS NULL OR m.category = :category) " +
            "AND (:materialCondition IS NULL OR m.materialCondition = :materialCondition) " +
            "AND (:transactionType IS NULL OR m.transactionType = :transactionType) " +
            "AND (:region IS NULL OR m.region = :region) " +
            "ORDER BY m.createdAt DESC")
    List<Material> findAllWithFilters(
            @Param("keyword") String keyword,
            @Param("category") MaterialCategory category,
            @Param("materialCondition") MaterialCondition materialCondition,
            @Param("transactionType") TransactionType transactionType,
            @Param("region") Region region
    );
}
