package org.sopt.certi_server.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.sopt.certi_server.global.jwt.util.JwtUtil;
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

    private static final List<String> EXCLUDE_URL_GET = Arrays.asList(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/auth/login-uri",
            "/api/v1/auth/reissue"
    );


    private static final List<String> EXCLUDE_URL_POST = Arrays.asList(
            "/api/v1/auth/sign-up",
            "/api/v1/auth/login"
    );

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.extractToken(request);
        boolean tokenExpired = jwtUtil.isTokenExpired(token);

        if(tokenExpired){
            throw new UnauthorizedException();
        }

        Long userId = jwtUtil.getUserId(token);
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
            return EXCLUDE_URL_GET.stream().anyMatch(exclude -> new AntPathMatcher().match(exclude, path));
        }

        if (method.equals(HttpMethod.POST.name())){
            return EXCLUDE_URL_POST.stream().anyMatch(exclude -> new AntPathMatcher().match(exclude, path));
        }
        return false;
    }
}
