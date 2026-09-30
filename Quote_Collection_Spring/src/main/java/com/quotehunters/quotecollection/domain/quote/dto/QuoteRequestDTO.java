package com.quotehunters.quotecollection.domain.quote.dto;

public class QuoteRequestDTO {
    private int personId;
    private int themeId;
    private String quote;

    public QuoteRequestDTO() {}

    public QuoteRequestDTO(int personId, int themeId, String quote) {
        this.personId = personId;
        this.themeId = themeId;
        this.quote = quote;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "QuoteRequestDTO{" +
                "personId=" + personId +
                ", themeId=" + themeId +
                ", quote='" + quote + '\'' +
                '}';
    }
}
