package com.quotehunters.quotecollection.domain.account.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "quote_user", uniqueConstraints =
        @UniqueConstraint(name = "uq_user_id_auth", columnNames = {"user_id", "user_auth"}))
public class AccountEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPw;

    @Column(name = "user_auth")
    private int auth;

    @Column(name = "daily_quote")
    private Integer dailyQuoteId;

    @Column(name = "daily_quote_date")
    private LocalDate dailyQuoteDate;

    public AccountEntity() {}

    public AccountEntity(int id, String userId, String userPw, int auth) {
        this.id = id;
        this.userId = userId;
        this.userPw = userPw;
        this.auth = auth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public Integer getDailyQuoteId() {
        return dailyQuoteId;
    }

    public void setDailyQuoteId(Integer dailyQuoteId) {
        this.dailyQuoteId = dailyQuoteId;
    }

    public LocalDate getDailyQuoteDate() {
        return dailyQuoteDate;
    }

    public void setDailyQuoteDate(LocalDate dailyQuoteDate) {
        this.dailyQuoteDate = dailyQuoteDate;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", auth='" + auth + '\'' +
                '}';
    }
}
