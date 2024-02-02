package com.heima.model.wemedia.dtos;


import lombok.Data;

@Data
public class WmLoginDto {

    /**
     * username
     */
    private String name;
    /**
     * password
     */
    private String password;
}
