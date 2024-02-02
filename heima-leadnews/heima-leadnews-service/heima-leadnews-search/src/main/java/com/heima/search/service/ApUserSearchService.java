package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {

    /**
     * Save the user's search history
     * @param keyword
     * @param userId
     */
    public void insert(String keyword, Integer userId);

    /**
     * Query search history
     * @return
     */
    public ResponseResult findUserSearch();

    /**
     * Delete history
     * @param dto
     * @return
     */
    public ResponseResult delUserSearch(HistorySearchDto dto);
}
