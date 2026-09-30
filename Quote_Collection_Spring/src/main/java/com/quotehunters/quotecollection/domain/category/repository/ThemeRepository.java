package com.quotehunters.quotecollection.domain.category.repository;

import com.quotehunters.quotecollection.domain.category.entity.ThemeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<ThemeEntity, Integer> {
    // 주제명 조회
    List<ThemeEntity> findByNameContaining(String name, Sort sort);

    // 중복 체크
    boolean existsByName(String name);
}
