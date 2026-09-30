package com.quotehunters.quotecollection.domain.account.repository;

import com.quotehunters.quotecollection.domain.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // id 찾기
    Optional<AccountEntity> findByUserIdAndAuth(String userId, int auth);

    // id 중복 체크
    boolean existsByUserIdAndAuth(String userId, int auth);
}
