package com.quotehunters.quotecollection.domain.mypage.bookmark.service;

import com.quotehunters.quotecollection.domain.account.entity.AccountEntity;
import com.quotehunters.quotecollection.domain.account.exception.NotFoundUserException;
import com.quotehunters.quotecollection.domain.account.repository.AccountRepository;
import com.quotehunters.quotecollection.domain.mypage.bookmark.dto.BookmarkDTO;
import com.quotehunters.quotecollection.domain.mypage.bookmark.dto.BookmarkRequestDTO;
import com.quotehunters.quotecollection.domain.mypage.bookmark.entity.BookmarkEntity;
import com.quotehunters.quotecollection.domain.mypage.bookmark.exception.NotFoundBookmarkException;
import com.quotehunters.quotecollection.domain.mypage.bookmark.exception.DuplicateBookmarkException;
import com.quotehunters.quotecollection.domain.mypage.bookmark.repository.BookmarkRepository;
import com.quotehunters.quotecollection.domain.quote.entity.QuoteEntity;
import com.quotehunters.quotecollection.domain.quote.exception.NotFoundQuoteException;
import com.quotehunters.quotecollection.domain.quote.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookmarkService {
    private static final Sort BOOKMARK_SORT = Sort.by("quote");

    private final BookmarkRepository bookmarkRepository;
    private final QuoteRepository quoteRepository;
    private final AccountRepository accountRepository;

    @Autowired
    private BookmarkService(
            BookmarkRepository bookmarkRepository,
            QuoteRepository quoteRepository,
            AccountRepository accountRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.quoteRepository = quoteRepository;
        this.accountRepository = accountRepository;
    }

    private AccountEntity findUserOrThrow(int userId) {
        return accountRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }

    private QuoteEntity findQuoteOrThrow(int quoteId) {
        return quoteRepository.findById(quoteId).orElseThrow(NotFoundQuoteException::new);
    }

    private BookmarkDTO convertToDTO(BookmarkEntity bookmarkEntity) {
        return new BookmarkDTO(
                bookmarkEntity.getId(),
                bookmarkEntity.getQuote().getPerson().getName(),
                bookmarkEntity.getQuote().getTheme().getName(),
                bookmarkEntity.getQuote().getQuote()
        );
    }

    // 내 전체 북마크
    public List<BookmarkDTO> findAllBookmarks(int memberId) {
        List<BookmarkEntity> bookmarks = bookmarkRepository.findAllByAccountId(memberId, BOOKMARK_SORT);

        return bookmarks.stream().map(this::convertToDTO).toList();
    }

    // 북마크 등록
    public void saveBookmark(int memberId, BookmarkRequestDTO bookmarkRequestDTO) {
        if (bookmarkRepository.existsByAccountIdAndQuoteId(memberId, bookmarkRequestDTO.getQuoteId())) {
            throw new DuplicateBookmarkException();
        }

        BookmarkEntity bookmarkEntity = new BookmarkEntity();

        bookmarkEntity.setAccount(findUserOrThrow(memberId));
        bookmarkEntity.setQuote(findQuoteOrThrow(bookmarkRequestDTO.getQuoteId()));
        bookmarkEntity.setCreatedAt(new Date());

        bookmarkRepository.save(bookmarkEntity);
    }

    // 북마크 취소
    public void bookmarkCancel(int memberId, int bookmarkId) {
        BookmarkEntity bookmark = bookmarkRepository
                .findByIdAndAccountId(bookmarkId, memberId)
                .orElseThrow(NotFoundBookmarkException::new);

        bookmarkRepository.delete(bookmark);
    }
}
