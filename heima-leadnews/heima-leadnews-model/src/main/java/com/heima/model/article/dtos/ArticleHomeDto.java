package com.heima.model.article.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // max time
    Date maxBehotTime;
    // min time
    Date minBehotTime;
    // page size
    Integer size;
    // channel ID
    String tag;
}