package com.BE.services.user;

import java.util.NoSuchElementException;

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
        return userRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
