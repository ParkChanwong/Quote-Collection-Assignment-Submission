package com.quotehunters.quotecollection.domain.person.repository;

import com.quotehunters.quotecollection.domain.person.entity.PersonEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
    // 국가명으로 인물 조회
    List<PersonEntity> findByCountryNameContaining(String countryName, Sort sort);

    // 시대명으로 인물 조회
    List<PersonEntity> findByPeriodNameContaining(String periodName, Sort sort);

    // 분야명으로 인물 조회
    List<PersonEntity> findByFieldNameContaining(String fieldName, Sort sort);

    // 인물명으로 인물 조회
    List<PersonEntity> findByNameContaining(String name, Sort sort);
}
