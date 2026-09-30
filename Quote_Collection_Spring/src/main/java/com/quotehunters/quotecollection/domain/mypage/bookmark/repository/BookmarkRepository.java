package com.quotehunters.quotecollection.domain.mypage.bookmark.repository;

import com.quotehunters.quotecollection.domain.mypage.bookmark.entity.BookmarkEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {
    boolean existsByAccountIdAndQuoteId(int accountId, int quoteId);

    // 내 모든 북마크 가져오기
    List<BookmarkEntity> findAllByAccountId(int userId, Sort sort);

    @Query("""
        SELECT b.quote.id
        FROM BookmarkEntity b
        WHERE b.account.id = :accountId
          AND b.quote.id IN :quoteIds
        """)
    List<Integer> findBookmarkedQuoteIds(
            @Param("accountId") int accountId,
            @Param("quoteIds") List<Integer> quoteIds
    );

    Optional<BookmarkEntity> findByIdAndAccountId(int bookmarkId, int userId);
}
