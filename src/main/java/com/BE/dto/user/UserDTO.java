package com.BE.dto.user;

import com.BE.constants.Role;
import com.BE.entities.User;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String name;
    private Role role;

    public UserDTO(User user){
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
    }
}
