package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     *
     * load Article list
     * @param dto
     * @param type  1。load more  2。load latest
     * @return
     */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

    /**
     * query article list of last 5 days
     * @param dayParam
     * @return
     */
    public List<ApArticle> findArticleListByLast5Days(@Param("dayParam") Date dayParam);
}
