package com.quotehunters.quotecollection.domain.mypage.bookmark.exception;

public class NotFoundBookmarkException extends RuntimeException {
    public NotFoundBookmarkException(String message) {
        super(message);
    }

    public NotFoundBookmarkException() {
        super("존재하지 않은 북마크입니다.");
    }
}
