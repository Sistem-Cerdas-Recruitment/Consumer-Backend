package com.BE.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BE.constants.Role;
import com.BE.dto.auth.AuthRequestDTO;
import com.BE.dto.auth.AuthResponseDTO;
import com.BE.dto.auth.RegisterRequestDTO;
import com.BE.entities.User;
import com.BE.security.JwtService;
import com.BE.services.user.UserService;

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

    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {

        Role role = Role.valueOf(registerRequest.getRole());

        if(userService.getUserByEmail(registerRequest.getEmail()) != null) {
            throw new IllegalArgumentException("User already exists");
        } else if(role != Role.CANDIDATE && role != Role.RECRUITER) {
            throw new IllegalArgumentException("Invalid role");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.valueOf(registerRequest.getRole()))
                .build();
        userService.saveUser(user);
        return AuthResponseDTO.builder().token(jwtService.generateToken(user)).build();

    }

    public AuthResponseDTO authenticate(AuthRequestDTO authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        User user = userService.getUserByEmail(authRequest.getEmail());
        return AuthResponseDTO.builder().token(jwtService.generateToken(user)).build();
    }

    public AuthResponseDTO refresh() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(username);
        return AuthResponseDTO.builder().token(jwtService.generateToken(user)).build();
    }
}
