package com.quotehunters.quotecollection.domain.quote.repository;

import com.quotehunters.quotecollection.domain.quote.entity.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Integer> {
    // 인물명 조회
    List<QuoteEntity> findAllQuotesByPersonNameContaining(String personName, Sort sort);

    // 주제명 조회
    List<QuoteEntity> findAllQuotesByThemeNameContaining(String themeName, Sort sort);

    // 키워드 조회
    List<QuoteEntity> findAllQuotesByQuoteContaining(String content, Sort sort);

    // 주제, 인물명 + 키워드 조회
    @Query("""
        SELECT q
        FROM QuoteEntity q
        JOIN q.person p
        JOIN q.theme t
        WHERE (:theme = '' OR t.name = :theme)
          AND (
              :keyword = ''
              OR q.quote LIKE CONCAT('%', :keyword, '%')
              OR p.name LIKE CONCAT('%', :keyword, '%')
          )
    """)
    Page<QuoteEntity> searchQuotes(
            @Param("theme") String theme,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 중복 체크
    boolean existsByPersonIdAndThemeIdAndQuote(int personId, int themeId, String quote);
}
