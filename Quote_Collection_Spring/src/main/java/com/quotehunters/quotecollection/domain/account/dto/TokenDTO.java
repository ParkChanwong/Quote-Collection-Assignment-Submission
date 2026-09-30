package com.quotehunters.quotecollection.domain.account.dto;

public class TokenDTO {
    private String token;
//    private String refreshToken;

    public TokenDTO(
            String token
//            String refreshToken
    ) {
        this.token = token;
//        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

//    public String getRefreshToken() {
//        return refreshToken;
//    }
}
