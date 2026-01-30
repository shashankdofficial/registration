package com.app.auth.config;

import com.app.auth.service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // String authHeader = request.getHeader("Authorization");
        String token = extractJwtFromCookie(request);

        // if (authHeader != null && authHeader.startsWith("Bearer ")) {

        //     String token = authHeader.substring(7);

        //     if (jwtUtil.isTokenValid(token)) {

        //         String username = jwtUtil.extractUsername(token);

        //         var userDetails =
        //                 userDetailsService.loadUserByUsername(username);

        //         UsernamePasswordAuthenticationToken authentication =
        //                 new UsernamePasswordAuthenticationToken(
        //                         userDetails,
        //                         null,
        //                         userDetails.getAuthorities()
        //                 );

        //         authentication.setDetails(
        //                 new WebAuthenticationDetailsSource()
        //                         .buildDetails(request)
        //         );

        //         SecurityContextHolder
        //                 .getContext()
        //                 .setAuthentication(authentication);
        //     }
        // }

        if (token != null && jwtUtil.isTokenValid(token)) {

            String username = jwtUtil.extractUsername(token);

            var userDetails =
                    userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
