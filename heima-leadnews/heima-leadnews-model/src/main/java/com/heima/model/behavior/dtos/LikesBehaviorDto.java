package com.heima.model.behavior.dtos;

import lombok.Data;

@Data
public class LikesBehaviorDto {


    // article ID,behavior ID,comment ID
    Long articleId;
    /**
     * the type of content
     * 0:article
     * 1:behavior
     * 2:comment
     */
    Short type;

    /**
     * the operation type of like
     * 0:like
     * 1:cancel like
     */
    Short operation;
}
