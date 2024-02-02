package com.heima.search.service;

import com.heima.model.search.dtos.UserSearchDto;
import com.heima.model.common.dtos.ResponseResult;

import java.io.IOException;

public interface ArticleSearchService {

    /**
     es article pagination search
     @return
     */
    ResponseResult searchArticle(UserSearchDto userSearchDto) throws IOException;
}