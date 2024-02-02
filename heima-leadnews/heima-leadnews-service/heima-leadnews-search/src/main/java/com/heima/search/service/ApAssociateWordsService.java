package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;

public interface ApAssociateWordsService {

    /**
     * search associational words
     * @param dto
     * @return
     */
    public ResponseResult associateSearch(UserSearchDto dto);
}
