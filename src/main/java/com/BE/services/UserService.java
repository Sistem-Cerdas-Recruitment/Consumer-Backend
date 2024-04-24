package com.BE.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.entities.User;
import com.BE.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public String createUser(String name) {
        userRepository.saveAndFlush(User.builder().name(name).build());
        return "User created successfully!";
    }

    public User getUser(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
