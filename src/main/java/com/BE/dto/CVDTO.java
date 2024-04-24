package com.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CVDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
}
