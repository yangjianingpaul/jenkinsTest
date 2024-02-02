package com.heima.model.user.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UserRelationDto {

    // Article author ID
    @IdEncrypt
    Integer authorId;

    // Article id
    @IdEncrypt
    Long articleId;
    /**
     * operating mode
     * 0  Follow
     * 1  Cancel
     */
    Short operation;
}