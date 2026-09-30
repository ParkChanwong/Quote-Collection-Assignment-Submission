package com.quotehunters.quotecollection.domain.category.repository;

import com.quotehunters.quotecollection.domain.category.entity.FieldEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
    // 분야명 조회
    List<FieldEntity> findByNameContaining(String name, Sort sort);

    // 중복 체크
    boolean existsByName(String name);
}
