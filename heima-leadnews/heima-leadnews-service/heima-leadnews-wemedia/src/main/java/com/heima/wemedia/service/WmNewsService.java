package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * Search the article list
     * @param dto
     * @return
     */
    public ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * Publish revised articles or save them as drafts
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * Articles listed or not
     * @param dto
     * @return
     */
    public ResponseResult downOrUp(WmNewsDto dto);

    /**
     * Delete article
     * @param id
     * @return
     */
    ResponseResult deleteNewsById(Integer id);

    /**
     * Article review list
     * @param dto
     * @return
     */
    ResponseResult authArticleList(NewsAuthDto dto);

    /**
     * Manual article review
     * @param articleId
     * @return
     */
    ResponseResult articleDetails(Integer articleId);

    /**
     * Manual approval
     * @param dto
     * @return
     */
    ResponseResult articleAuthPass(NewsAuthDto dto);

    /**
     * Manual audit failure
     * @param dto
     * @return
     */
    ResponseResult articleAuthFail(NewsAuthDto dto);

    /**
     * update article
     * @param wemediaId
     * @return
     */
    ResponseResult getWemediaById(Integer wemediaId);
}
