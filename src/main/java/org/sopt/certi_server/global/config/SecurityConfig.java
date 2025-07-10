package org.sopt.certi_server.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.global.jwt.core.JwtExtractor;
import org.sopt.certi_server.global.filter.ExceptionHandlerFilter;
import org.sopt.certi_server.global.filter.JwtAuthenticationFilter;
import org.sopt.certi_server.global.jwt.core.JwtValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] WHITELIST = {
        "/api/v1/auth/login-uri",
        "/api/v1/auth/login",
        "/api/v1/auth/sign-up",
        "/api/v1/auth/sign-in",
        "/api/v1/auth/reissue"
    };


    private final JwtExtractor jwtExtractor;
    private final JwtValidator jwtValidator;
    private final ObjectMapper objectMapper;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(HttpMethod.OPTIONS)
                    .permitAll() //OPTION추가
                    .requestMatchers(WHITELIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated());

        http
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtExtractor, jwtValidator), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(objectMapper), JwtAuthenticationFilter.class);
        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
