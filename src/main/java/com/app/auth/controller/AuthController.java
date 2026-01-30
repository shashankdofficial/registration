package com.app.auth.controller;

import com.app.auth.config.JwtUtil;
import com.app.auth.dto.LoginRequest;
import com.app.auth.dto.SignupRequest;
import com.app.auth.entity.User;
import com.app.auth.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // ======================
    // SIGNUP
    // ======================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {

        User user = userService.register(
                request.getUsername(),
                request.getPassword());

        return ResponseEntity.ok(
                Map.of("message", "User registered successfully"));
    }

    // ======================
    // LOGIN
    // ======================
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

    // Authentication authentication =
    // authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(
    // request.getUsername(),
    // request.getPassword()
    // )
    // );

    // SecurityContextHolder.getContext()
    // .setAuthentication(authentication);

    // String token = jwtUtil.generateToken(request.getUsername());

    // return ResponseEntity.ok(
    // Map.of("token", token)
    // );
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String token = jwtUtil.generateToken(request.getUsername());

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        // jwtCookie.setSecure(false); // true in HTTPS (prod)
        jwtCookie.setSecure(true); // true in HTTPS (prod)
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60); // 1 hour

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(
                Map.of("message", "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

}
