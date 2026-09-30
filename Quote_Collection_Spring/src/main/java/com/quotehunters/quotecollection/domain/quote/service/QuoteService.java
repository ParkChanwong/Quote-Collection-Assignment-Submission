package com.quotehunters.quotecollection.domain.quote.service;

import com.quotehunters.quotecollection.domain.account.entity.AccountEntity;
import com.quotehunters.quotecollection.domain.account.exception.NotFoundUserException;
import com.quotehunters.quotecollection.domain.account.repository.AccountRepository;
import com.quotehunters.quotecollection.domain.mypage.bookmark.repository.BookmarkRepository;
import com.quotehunters.quotecollection.domain.quote.dto.BookmarkResponseDTO;
import com.quotehunters.quotecollection.domain.quote.dto.QuoteRequestDTO;
import com.quotehunters.quotecollection.domain.quote.dto.QuoteResponseDTO;
import com.quotehunters.quotecollection.domain.category.entity.ThemeEntity;
import com.quotehunters.quotecollection.domain.person.entity.PersonEntity;
import com.quotehunters.quotecollection.domain.quote.entity.QuoteEntity;
import com.quotehunters.quotecollection.domain.category.exception.theme.NotFoundThemeException;
import com.quotehunters.quotecollection.domain.person.exception.NotFoundPersonException;
import com.quotehunters.quotecollection.domain.quote.exception.DuplicateQuoteException;
import com.quotehunters.quotecollection.domain.quote.exception.EmptyQuoteException;
import com.quotehunters.quotecollection.domain.quote.exception.NotFoundQuoteException;
import com.quotehunters.quotecollection.domain.category.repository.ThemeRepository;
import com.quotehunters.quotecollection.domain.person.repository.PersonRepository;
import com.quotehunters.quotecollection.domain.quote.repository.QuoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuoteService {
    private static final Sort QUOTE_SORT = Sort.by("quote", "themeName", "personName");

    private final QuoteRepository quoteRepository;
    private final PersonRepository personRepository;
    private final ThemeRepository themeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public QuoteService(
            QuoteRepository quoteRepository,
            PersonRepository personRepository,
            ThemeRepository themeRepository,
            BookmarkRepository bookmarkRepository,
            AccountRepository accountRepository
    ) {
        this.quoteRepository = quoteRepository;
        this.personRepository = personRepository;
        this.themeRepository = themeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.accountRepository = accountRepository;
    }

    private Set<Integer> findBookmarkedIds(
            Integer accountId,
            List<QuoteEntity> quotes
    ) {
        // 비로그인 또는 검색 결과가 없으면 북마크 조회 생략
        if (accountId == null || quotes.isEmpty()) {
            return Set.of();
        }

        List<Integer> quoteIds = quotes.stream()
                .map(QuoteEntity::getId)
                .toList();

        return new HashSet<>(
                bookmarkRepository.findBookmarkedQuoteIds(accountId, quoteIds)
        );
    }

    private BookmarkResponseDTO convertToDTO(
            QuoteEntity quote,
            Set<Integer> bookmarkedIds
    ) {
        return new BookmarkResponseDTO(
                quote.getId(),
                quote.getPerson().getName(),
                quote.getTheme().getName(),
                quote.getQuote(),
                bookmarkedIds.contains(quote.getId())
        );
    }

    private QuoteResponseDTO convertToDTO(QuoteEntity quoteEntity) {
        return new QuoteResponseDTO(
                quoteEntity.getId(),
                quoteEntity.getPerson().getName(),
                quoteEntity.getTheme().getName(),
                quoteEntity.getQuote()
        );
    }

    private PersonEntity findPersonOrThrow(int personId) {
        return personRepository
                .findById(personId)
                .orElseThrow(NotFoundPersonException::new);
    }

    private ThemeEntity findThemeOrThrow(int themeId) {
        return themeRepository
                .findById(themeId)
                .orElseThrow(NotFoundThemeException::new);
    }

    private QuoteEntity convertToEntity(
            String content,
            PersonEntity personEntity,
            ThemeEntity themeEntity
    ) {
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setPerson(personEntity);
        quoteEntity.setTheme(themeEntity);
        quoteEntity.setQuote(content);

        return quoteEntity;
    }

    private List<QuoteEntity> dailyQuote() {
        long count = quoteRepository.count();

        if (count == 0) {
            throw new EmptyQuoteException();
        }

        int offset = ThreadLocalRandom.current().nextInt(Math.toIntExact(count));

        Pageable pageable = PageRequest.of(offset, 1, QUOTE_SORT);

        return quoteRepository.findAll(pageable).getContent();
    }

    // 전체 명언 조회
    public List<QuoteResponseDTO> findAllQuotes() {
        List<QuoteEntity> quotes = quoteRepository.findAll(QUOTE_SORT);

        return quotes.stream().map(this::convertToDTO).toList();
    }

    // 전체 명언 조회 - 페이지네이션
    public Page<QuoteResponseDTO> findAllQuotesByPage(Pageable pageable) {
        Page<QuoteEntity> quotes = quoteRepository.findAll(pageable);

        return quotes.map(this::convertToDTO);
    }

    // 인물명으로 명언 조회
    public List<QuoteResponseDTO> findAllQuotesByPersonName(String personName) {
        List<QuoteEntity> quotes = quoteRepository.findAllQuotesByPersonNameContaining(personName, QUOTE_SORT);

        return quotes.stream().map(this::convertToDTO).toList();
    }

    // 주제명으로 명언 조회
    public List<QuoteResponseDTO> findAllQuotesByThemeName(String themeName) {
        List<QuoteEntity> quotes = quoteRepository.findAllQuotesByThemeNameContaining(themeName, QUOTE_SORT);

        return quotes.stream().map(this::convertToDTO).toList();
    }

    // 키워드로 명언 조회
    public List<QuoteResponseDTO> findAllQuotesByKeyword(String keyword) {
        List<QuoteEntity> quotes = quoteRepository.findAllQuotesByQuoteContaining(keyword, QUOTE_SORT);

        return quotes.stream().map(this::convertToDTO).toList();
    }

    // 명언 ID 단일 조회
    public QuoteResponseDTO findQuoteById(int quoteId) {
        QuoteEntity quoteEntity = quoteRepository.
                findById(quoteId).
                orElseThrow(NotFoundQuoteException::new);

        return convertToDTO(quoteEntity);
    }

    // 주제, 인물명 + 키워드 조회
    public Page<BookmarkResponseDTO> searchQuotes(Integer accountId, String themeName, String keyword, Pageable pageable) {
        Page<QuoteEntity> quotes = quoteRepository.searchQuotes(themeName, keyword, pageable);

        Set<Integer> bookmarkedIds = findBookmarkedIds(accountId, quotes.getContent());

        return quotes.map(quote -> convertToDTO(quote, bookmarkedIds));
    }

    // 오늘의 명언
    @Transactional
    public BookmarkResponseDTO findDailyQuote(Integer accountId) {
        AccountEntity account = accountRepository
                .findById(accountId)
                .orElseThrow(NotFoundUserException::new);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        List<QuoteEntity> quotes = new ArrayList<>();

        if (account.getDailyQuoteId() == null || !today.equals(account.getDailyQuoteDate())) {
            quotes = dailyQuote();

            account.setDailyQuoteId(quotes.get(0).getId());
            account.setDailyQuoteDate(today);
        } else {
            QuoteEntity savedQuote = quoteRepository
                    .findById(account.getDailyQuoteId())
                    .orElseThrow(NotFoundQuoteException::new);

            quotes = List.of(savedQuote);
        }

        Set<Integer> bookmarkedIds = findBookmarkedIds(accountId, quotes);

        return convertToDTO(quotes.get(0), bookmarkedIds);
    }

    // 명언 등록
    @Transactional
    public void saveQuote(QuoteRequestDTO quoteDTO) {
        PersonEntity personEntity = findPersonOrThrow(quoteDTO.getPersonId());
        ThemeEntity themeEntity = findThemeOrThrow(quoteDTO.getThemeId());

        if (quoteDTO.getQuote() == null || quoteDTO.getQuote().isBlank()) {
            throw new EmptyQuoteException();
        }
        String content = quoteDTO.getQuote().trim();
        if (
            quoteRepository.existsByPersonIdAndThemeIdAndQuote(
                personEntity.getId(),
                themeEntity.getId(),
                content
            )
        ) {
            throw new DuplicateQuoteException();
        }

        quoteRepository.save(convertToEntity(content, personEntity, themeEntity));
    }

    // 명언 수정
    @Transactional
    public void modifyQuote(int id, QuoteRequestDTO quoteDTO) {
        QuoteEntity quote = quoteRepository.findById(id).orElseThrow(NotFoundQuoteException::new);

        PersonEntity person = findPersonOrThrow(quoteDTO.getPersonId());
        ThemeEntity theme = findThemeOrThrow(quoteDTO.getThemeId());

        if (quoteDTO.getQuote() == null || quoteDTO.getQuote().isBlank()) {
            throw new EmptyQuoteException();
        }
        String content = quoteDTO.getQuote().trim();
        if (
            quoteRepository.existsByPersonIdAndThemeIdAndQuote(
                person.getId(),
                theme.getId(),
                content
            )
        ) {
            throw new DuplicateQuoteException();
        }

        quote.setPerson(person);
        quote.setTheme(theme);
        quote.setQuote(content);
    }

    // 명언 삭제
    @Transactional
    public void deleteQuote(int id) {
        QuoteEntity quote = quoteRepository.findById(id).orElseThrow(NotFoundQuoteException::new);

        quoteRepository.delete(quote);
    }
}
