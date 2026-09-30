package com.quotehunters.quotecollection.domain.account.dto;

public class AccountDTO {
    private String userId;
    private String userPw;

    public AccountDTO() {}

    public AccountDTO(String userId, String userPw) {
        this.userId = userId;
        this.userPw = userPw;
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

    @Override
    public String toString() {
        return "SignInDTO{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
