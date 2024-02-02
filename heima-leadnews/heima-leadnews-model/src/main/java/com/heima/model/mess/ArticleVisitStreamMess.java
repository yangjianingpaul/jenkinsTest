package com.heima.model.mess;

import lombok.Data;

@Data
public class ArticleVisitStreamMess {
    /**
     * article id
     */
    private Long articleId;
    /**
     * read
     */
    private int view;
    /**
     * collection
     */
    private int collect;
    /**
     * comments
     */
    private int comment;
    /**
     * like
     */
    private int like;
}