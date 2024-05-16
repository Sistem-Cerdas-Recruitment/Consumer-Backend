package com.BE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.user.UserDTO;
import com.BE.services.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDTO> getUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO(userService.getUserByEmail(username));
        return ResponseEntity.ok(userDTO);
    }

}
