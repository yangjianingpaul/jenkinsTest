package com.heima.model.user.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class AuthDto  extends PageRequestDto {

    /**
     * status
     */
    private Short status;

    private Integer id;

    //Rejected message
    private String msg;
}
