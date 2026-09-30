package com.quotehunters.quotecollection.domain.quote.entity;

import com.quotehunters.quotecollection.domain.category.entity.ThemeEntity;
import com.quotehunters.quotecollection.domain.person.entity.PersonEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "quote")
public class QuoteEntity {
    @Id
    @Column(name = "quote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private ThemeEntity theme;

    @Column(name = "quote_content")
    private String quote;

    public QuoteEntity() {}

    public QuoteEntity(int id, PersonEntity person, ThemeEntity theme, String quote) {
        this.id = id;
        this.person = person;
        this.theme = theme;
        this.quote = quote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public ThemeEntity getTheme() {
        return theme;
    }

    public void setTheme(ThemeEntity theme) {
        this.theme = theme;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "QuoteEntity{" +
                "id=" + id +
                ", person=" + person +
                ", theme=" + theme +
                ", quote='" + quote + '\'' +
                '}';
    }
}
