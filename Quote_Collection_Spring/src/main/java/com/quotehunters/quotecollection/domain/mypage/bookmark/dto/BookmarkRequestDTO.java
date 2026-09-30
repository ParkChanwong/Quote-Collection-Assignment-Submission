package com.quotehunters.quotecollection.domain.mypage.bookmark.dto;

public class BookmarkRequestDTO {
    private int quoteId;

    public BookmarkRequestDTO() {}

    public BookmarkRequestDTO(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }
}
