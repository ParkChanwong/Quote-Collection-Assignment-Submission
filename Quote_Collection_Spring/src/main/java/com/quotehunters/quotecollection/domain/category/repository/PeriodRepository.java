package com.quotehunters.quotecollection.domain.category.repository;

import com.quotehunters.quotecollection.domain.category.entity.PeriodEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<PeriodEntity, Integer> {
    // 시대명 조회
    List<PeriodEntity> findByNameContaining(String name, Sort sort);

    // 중복 체크
    boolean existsByName(String name);
}
