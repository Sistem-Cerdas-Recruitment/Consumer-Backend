package com.BE.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BE.dto.auth.AuthRequest;
import com.BE.dto.auth.AuthResponse;
import com.BE.dto.auth.RegisterRequest;
import com.BE.entities.Role;
import com.BE.entities.User;
import com.BE.security.JwtService;
import com.BE.services.UserService;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {

        if(userService.getUserByEmail(registerRequest.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.CANDIDATE)
                .build();
        userService.saveUser(user);
        return AuthResponse.builder().token(jwtService.generateToken(user)).build();

    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        User user = userService.getUserByEmail(authRequest.getEmail());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return AuthResponse.builder().token(jwtService.generateToken(user)).build();
    }
}
