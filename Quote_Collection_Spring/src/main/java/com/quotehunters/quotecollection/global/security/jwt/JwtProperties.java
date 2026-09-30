package com.quotehunters.quotecollection.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, String issuer, Duration accessTokenExpiration) {
}
