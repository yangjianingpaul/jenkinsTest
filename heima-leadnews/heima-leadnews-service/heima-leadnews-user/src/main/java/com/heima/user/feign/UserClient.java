package com.heima.user.feign;

import com.heima.apis.user.IUserClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserClient implements IUserClient {

    @Autowired
    private ApUserService apUserService;

    @GetMapping("/api/v1/user/{userId}}")
    @Override
    public ResponseResult getUserName(Integer userId) {
        return apUserService.getUserName(userId);
    }
}
