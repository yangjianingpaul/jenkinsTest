package com.heima.model.search.vos;

import lombok.Data;
import java.util.Date;

@Data
public class SearchArticleVo {

    // article id
    private Long id;
    // title of the article
    private String title;
    // date of publication of the article
    private Date publishTime;
    // article layout
    private Integer layout;
    // cover
    private String images;
    // author id
    private Long authorId;
    // Author's name
    private String authorName;
    //static urls
    private String staticUrl;
    //article content
    private String content;

}