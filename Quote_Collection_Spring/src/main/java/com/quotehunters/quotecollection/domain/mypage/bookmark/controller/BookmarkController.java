package com.quotehunters.quotecollection.domain.mypage.bookmark.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.domain.mypage.bookmark.dto.BookmarkDTO;
import com.quotehunters.quotecollection.domain.mypage.bookmark.dto.BookmarkRequestDTO;
import com.quotehunters.quotecollection.domain.mypage.bookmark.service.BookmarkService;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "북마크", description = "내 북마크 조회·등록·취소 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/mypage")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // 내 전체 북마크
    @Operation(summary = "내 북마크 조회",
            description = "로그인한 회원의 북마크 목록을 조회합니다.")
    @GetMapping("/bookmark")
    public ResponseEntity<ResponseList<BookmarkDTO>> findAllBookmarks(@AuthenticationPrincipal Jwt jwt) {
        int myId = Integer.parseInt(Objects.requireNonNull(jwt.getSubject()));
        List<BookmarkDTO> bookmarks = bookmarkService.findAllBookmarks(myId);

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), bookmarks));
    }

    // 북마크 등록
    @Operation(summary = "북마크 등록",
            description = "로그인한 회원의 북마크에 명언을 추가합니다.")
    @PostMapping("/bookmark")
    public ResponseEntity<ResponseSingle<String>> saveBookmark(@AuthenticationPrincipal Jwt jwt, @RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
        int myId = Integer.parseInt(Objects.requireNonNull(jwt.getSubject()));

        bookmarkService.saveBookmark(myId, bookmarkRequestDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "북마크 등록 성공"));
    }

    // 북마크 취소
    @Operation(summary = "북마크 취소",
            description = "로그인한 회원의 북마크를 ID로 삭제합니다.")
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<ResponseSingle<String>> bookmarkCancel(@AuthenticationPrincipal Jwt jwt, @PathVariable int bookmarkId) {
        int myId = Integer.parseInt(Objects.requireNonNull(jwt.getSubject()));

        bookmarkService.bookmarkCancel(myId, bookmarkId);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "북마크가 취소되었습니다."));
    }
}
