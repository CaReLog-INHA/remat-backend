package com.remat.domain.material.repository;

import com.remat.domain.material.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByIdAndDeletedAtIsNull(Long id);
}
