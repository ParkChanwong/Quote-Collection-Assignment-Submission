package com.quotehunters.quotecollection.domain.mypage.bookmark.entity;

import com.quotehunters.quotecollection.domain.account.entity.AccountEntity;
import com.quotehunters.quotecollection.domain.quote.entity.QuoteEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bookmark")
public class BookmarkEntity {
    @Id
    @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private QuoteEntity quote;

    @Column(name = "create_date")
    private Date createdAt;

    public BookmarkEntity() {}

    public BookmarkEntity(int id, AccountEntity account, QuoteEntity quote, Date createdAt) {
        this.id = id;
        this.account = account;
        this.quote = quote;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public QuoteEntity getQuote() {
        return quote;
    }

    public void setQuote(QuoteEntity quote) {
        this.quote = quote;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BookmarkEntity{" +
                "id=" + id +
                ", account=" + account +
                ", quote=" + quote +
                ", createdAt=" + createdAt +
                '}';
    }
}
