package com.heima.model.mess;

import lombok.Data;

@Data
public class UpdateArticleMess {

    /**
     * modify the field type of the article
      */
    private UpdateArticleType type;
    /**
     * article id
     */
    private Long articleId;
    /**
     * Modify the increment of the data, which can be positive or negative
     */
    private Integer add;

    public enum UpdateArticleType{
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}