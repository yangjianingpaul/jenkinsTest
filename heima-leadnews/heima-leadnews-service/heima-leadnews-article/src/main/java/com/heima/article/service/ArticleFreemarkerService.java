package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

public interface ArticleFreemarkerService {
    /**
     * Generate a static file and upload it to the min IO file
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinIO(ApArticle apArticle, String content);

    /**
     * delete the min io static file
     * @param staticUrl
     */
    void deleteArticleToMinio(String staticUrl);
}
