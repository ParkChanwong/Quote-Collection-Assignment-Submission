package com.quotehunters.quotecollection.domain.quote.dto;

public class QuoteResponseDTO {
    private int id;
    private String personName;
    private String themeName;
    private String quote;

    public QuoteResponseDTO() {}

    public QuoteResponseDTO(int id, String personName, String themeName, String quote) {
        this.id = id;
        this.personName = personName;
        this.themeName = themeName;
        this.quote = quote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "QuoteResponseDTO{" +
                "id=" + id +
                ", personName='" + personName + '\'' +
                ", themeName='" + themeName + '\'' +
                ", quote='" + quote + '\'' +
                '}';
    }
}
