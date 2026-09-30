package com.quotehunters.quotecollection.domain.quote.dto;

public class BookmarkResponseDTO extends QuoteResponseDTO {
    boolean bookmark;

    public BookmarkResponseDTO(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public BookmarkResponseDTO(int id, String personName, String themeName, String quote, boolean bookmark) {
        super(id, personName, themeName, quote);
        this.bookmark = bookmark;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "bookmark=" + bookmark +
                '}';
    }
}
