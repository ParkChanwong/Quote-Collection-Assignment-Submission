package com.quotehunters.quotecollection.global.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final String[] CONTENT_PATHS = {
            "/quote", "/quote/**",
            "/person", "/person/**",
            "/country", "/country/**",
            "/period", "/period/**",
            "/field", "/field/**",
            "/theme", "/theme/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> {})
                // 쿠키 인증 없이 Authorization 헤더의 Bearer JWT만 사용
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        // swagger UI와 API 명세는 로그인 없이 접근 허용
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 앞으로 만들 회원가입·로그인 경로
                        .requestMatchers(
                                HttpMethod.POST,
                                "/account/signup/**",
                                "/account/signin"
                        ).permitAll()

                        // 명언·인물·분류 조회는 공개
                        .requestMatchers(HttpMethod.GET, CONTENT_PATHS)
                        .permitAll()

                        // 위 경로의 등록·수정·삭제는 관리자만
                        .requestMatchers(CONTENT_PATHS)
                        .hasRole("ADMIN")

                        // 그 외 요청은 로그인 필요
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))

                .oauth2ResourceServer(resourceServer -> resourceServer
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins}") List<String> allowedOrigins
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        allowedOrigins.add("http://localhost:5173");
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            response.setStatus(401);
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "statusCode": 401,
                  "message": "로그인이 필요하거나 토큰이 유효하지 않습니다."
                }
                """);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                  "statusCode": 403,
                  "message": "접근 권한이 없습니다."
                }
                """);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
