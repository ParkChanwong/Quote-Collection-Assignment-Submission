package com.quotehunters.quotecollection.domain.mypage.bookmark.exception;

public class DuplicateBookmarkException extends RuntimeException {
    public DuplicateBookmarkException() {
        super("이미 등록한 북마크입니다.");
    }
}
