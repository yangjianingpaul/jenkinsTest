package com.heima.user.service;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;


public interface ApUserRelationService {
    /**
     * User follow/unfollow
     * @param dto
     * @return
     */
    public ResponseResult follow(UserRelationDto dto);
}