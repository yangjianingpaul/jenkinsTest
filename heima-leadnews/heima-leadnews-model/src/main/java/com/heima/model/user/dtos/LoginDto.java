package com.heima.model.user.dtos;


import lombok.Data;

@Data
public class LoginDto {

    /**
     * Phone number
     */
    private String phone;

    /**
     * password
     */
    private String password;
}
