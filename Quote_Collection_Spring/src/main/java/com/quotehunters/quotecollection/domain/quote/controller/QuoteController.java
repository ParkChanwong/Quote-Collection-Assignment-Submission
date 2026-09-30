package com.quotehunters.quotecollection.domain.quote.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.domain.quote.dto.BookmarkResponseDTO;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.quote.dto.QuoteRequestDTO;
import com.quotehunters.quotecollection.domain.quote.dto.QuoteResponseDTO;
import com.quotehunters.quotecollection.domain.quote.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "명언", description = "명언 조회·검색·등록·수정·삭제 API")
@RestController
@RequestMapping("/quote")
public class QuoteController {
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    // 전체 명언 조회
    @Operation(summary = "명언 목록 조회",
            description = "page를 생략하면 전체 명언 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<QuoteResponseDTO>> findAllQuotes() {
        List<QuoteResponseDTO> quotes = quoteService.findAllQuotes();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), quotes));
    }

    // 전체 명언 조회 - 페이지네이션
    @Operation(summary = "명언 목록 조회",
            description = "page를 생략하면 전체 명언 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<QuoteResponseDTO>> findAllQuotesByPage(
            @PageableDefault(
                    size = 10,
                    sort = {"quote", "themeName", "personName"}
            ) @ParameterObject Pageable pageable
    ) {
        Page<QuoteResponseDTO> quotes = quoteService.findAllQuotesByPage(pageable);

        return ResponseEntity.ok(new ResponsePage<>(HttpStatus.OK.value(), quotes.getContent(), quotes.getTotalElements(), quotes.getTotalPages()));
    }


    // 인물명으로 명언 조회
    @Operation(summary = "인물명으로 명언 검색",
            description = "keyword로 인물명을 검색하여 명언 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/person")
    public ResponseEntity<ResponseList<QuoteResponseDTO>> findQuotesByPerson(@RequestParam String keyword) {
        List<QuoteResponseDTO> quotes = quoteService.findAllQuotesByPersonName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), quotes));
    }

    // 주제명으로 명언 조회
    @Operation(summary = "주제명으로 명언 검색",
            description = "keyword로 주제명을 검색하여 명언 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/theme")
    public ResponseEntity<ResponseList<QuoteResponseDTO>> findQuotesByTheme(@RequestParam String keyword) {
        List<QuoteResponseDTO> quotes = quoteService.findAllQuotesByThemeName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), quotes));
    }

    // 키워드로 명언 조회
    @Operation(summary = "내용으로 명언 검색",
            description = "keyword로 내용을 검색하여 명언 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/content")
    public ResponseEntity<ResponseList<QuoteResponseDTO>> findQuotesByKeyword(@RequestParam String keyword) {
        List<QuoteResponseDTO> quotes = quoteService.findAllQuotesByKeyword(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), quotes));
    }

    // 명언 ID 단일 조회
    @Operation(summary = "명언 단일 조회",
            description = "ID로 명언 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<QuoteResponseDTO>> findQuoteById(@PathVariable int id) {
        QuoteResponseDTO quote = quoteService.findQuoteById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), quote));
    }

    // 주제, 인물명 + 키워드 조회
    @Operation(summary = "명언 검색",
            description = "주제와 검색어로 명언을 페이지 단위로 조회합니다. 로그인 없이 조회할 수 있으며, JWT를 전달하면 해당 회원의 북마크 여부를 함께 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<ResponsePage<BookmarkResponseDTO>> searchQuotes(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam String theme,
        @RequestParam String keyword,
        @PageableDefault(
                size = 10,
                sort = {"quote", "person.name", "theme.name", "id"}
        ) @ParameterObject Pageable pageable
    ) {
        Integer accountId =
                jwt == null ? null : Integer.valueOf(jwt.getSubject());

        Page<BookmarkResponseDTO> quotes =
                quoteService.searchQuotes(accountId, theme.trim(), keyword.trim(), pageable);

        return ResponseEntity.ok(
                new ResponsePage<>(
                        HttpStatus.OK.value(),
                        quotes.getContent(),
                        quotes.getTotalElements(),
                        quotes.getTotalPages()
                )
        );
    }

    // 오늘의 명언
    @Operation(summary = "오늘의 명언 조회",
            description = "오늘의 명언을 조회합니다. 로그인 없이 조회할 수 있으며, JWT를 전달하면 해당 회원의 북마크 여부를 함께 반환합니다.")
    @GetMapping("/daily")
    public ResponseEntity<ResponseSingle<BookmarkResponseDTO>> findDailyQuote(@AuthenticationPrincipal Jwt jwt) {
        Integer accountId =
                jwt == null ? null : Integer.valueOf(jwt.getSubject());

        System.out.println("jwt 존재: " + (jwt != null));
        System.out.println("accountId: " + accountId);

        BookmarkResponseDTO quote = quoteService.findDailyQuote(accountId);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), quote));
    }

    // 명언 등록
    @Operation(summary = "명언 등록",
            description = "명언 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> saveQuote(@RequestBody QuoteRequestDTO quoteRequestDTO) {
        quoteService.saveQuote(quoteRequestDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "명언 등록 성공"));
    }

    // 명언 수정
    @Operation(summary = "명언 수정",
            description = "명언 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> modifyQuote(@PathVariable int id, @RequestBody QuoteRequestDTO quoteRequestDTO) {
        quoteService.modifyQuote(id, quoteRequestDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "명언 수정 성공"));
    }

    // 명언 삭제
    @Operation(summary = "명언 삭제",
            description = "명언 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deleteQuote(@PathVariable int id) {
        quoteService.deleteQuote(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "명언 삭제 성공"));
    }
}
