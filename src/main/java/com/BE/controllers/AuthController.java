package com.BE.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.auth.AuthRequest;
import com.BE.dto.auth.AuthResponse;
import com.BE.dto.auth.RegisterRequest;
import com.BE.services.auth.AuthService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            ResponseCookie cookie = ResponseCookie.from("biskuat", response.getToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30)
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Set-Cookie", cookie.toString());
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse response = authService.authenticate(authRequest);
            ResponseCookie cookie = ResponseCookie.from("biskuat", response.getToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30)
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Set-Cookie", cookie.toString());
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("biskuat", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }
}
