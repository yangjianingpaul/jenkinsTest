package com.heima.article.service;

import com.heima.model.article.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApCollectionService {

    /**
     * collection
     * @param dto
     * @return
     */
    public ResponseResult collection(CollectionBehaviorDto dto);
}
