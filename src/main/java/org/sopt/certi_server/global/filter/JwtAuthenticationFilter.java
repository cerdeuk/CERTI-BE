package org.sopt.certi_server.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.sopt.certi_server.global.jwt.core.JwtExtractor;
import org.sopt.certi_server.global.jwt.core.JwtValidator;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URL = Arrays.asList(
        "/api/v1/auth/login-uri",
        "/api/v1/auth/login",
        "/api/v1/auth/sign-up",
        "/api/v1/auth/reissue",
        "/api/v1/careers/**",
        "/api/v1/activity/**",
        "/api/v1/prior-certification/**",
        "/api/v1/home/**",
        "/api/v1/certification/**",
        "/api/v1/**"
    );

    private final JwtExtractor jwtExtractor;
    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = jwtExtractor.extractToken(authorization);
        boolean tokenExpired = jwtValidator.isExpired(token);

        if(tokenExpired){
            throw new UnauthorizedException();
        }

        Long userId = jwtExtractor.extractUserId(token);
        authenticate(request, userId);
        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, Long userId) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, null));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();

        if (method.equals(HttpMethod.GET.name())) {
            return EXCLUDE_URL.stream().anyMatch(exclude -> new AntPathMatcher().match(exclude, path));
        }

        if (method.equals(HttpMethod.POST.name())) {
            return EXCLUDE_URL.stream().anyMatch(exclude -> new AntPathMatcher().match(exclude, path));
        }
        return false;
    }
}
