package com.heima.wemedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.article.IArticleClient;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.constants.WmNewsMessageConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {


    /**
     * a list of conditional query articles
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmNewsPageReqDto dto) {
//        1。query parameters
//        pagination check
        dto.checkParam();
//        2。pagination condition query
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper();
//        accurate status queries
        if (dto.getStatus() != null) {
            lambdaQueryWrapper.eq(WmNews::getStatus, dto.getStatus());
        }
//        channel precise query
        if (dto.getChannelId() != null) {
            lambdaQueryWrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }
//        time range query
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            lambdaQueryWrapper.between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }
//        fuzzy queries for keywords
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            lambdaQueryWrapper.like(WmNews::getTitle, dto.getKeyword() );
        }
//        query the articles of the current logged in person
        lambdaQueryWrapper.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());
//        query in reverse order of release time
        lambdaQueryWrapper.orderByDesc(WmNews::getPublishTime);
        page = page(page, lambdaQueryWrapper);
//        3。the results are returned
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Autowired
    private WmNewsTaskService wmNewsTaskService;

    /**
     * Publish revised articles or save them as drafts
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        if (dto == null || dto.getContent() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID );
        }
//        1.Save or modify the article
        WmNews wmNews = new WmNews();
//        Attribute copy
        BeanUtils.copyProperties(dto,wmNews);
        if (dto.getImages() != null && dto.getImages().size() > 0) {
            String imagesStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imagesStr);
        }

        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            wmNews.setType(null);
        }

        saveOrUpdateWmNews(wmNews);

//        2.Determine whether it is a draft, and if it is a draft end current method
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
//        3.Not a draft, save the article content picture material relationship
//        Get the picture information in the article content
        List<String> materials = extractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials, wmNews.getId());
//        4.Not a draft, save the relationship between the article cover picture and the material
        saveRelativeInfoForCover(dto, wmNews, materials);

//        5.Review an article
        wmNewsTaskService.addNewsToTask(wmNews.getId(), wmNews.getPublishTime());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * the article is listed or not
     * @param dto
     * @return
     */
    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
//        1：check the parameters
        if (dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
//        2：query the article
        WmNews wmNews = getById(dto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "the article does not exist");
        }
//        3：determine if the article has been published
        if (!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "The current article is not published and cannot be taken or unlisted");
        }
//        4:modify the article enable
        if (dto.getEnable() != null && dto.getEnable() > -1 && dto.getEnable() < 2) {
            update(Wrappers.<WmNews>lambdaUpdate().set(WmNews::getEnable, dto.getEnable())
                    .eq(WmNews::getId, wmNews.getId()));
            if (wmNews.getArticleId() != null) {
//            Send a message informing the article to modify the configuration of the article
                Map<String, Object> map = new HashMap<>();
                map.put("articleId", wmNews.getArticleId());
                map.put("enable", dto.getEnable());
                kafkaTemplate.send(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC,
                        JSON.toJSONString(map));
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private IArticleClient articleClient;

    /**
     * delete the article
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteNewsById(Integer id) {
        WmNews wmNews = getById(id);
        if (wmNews.getStatus() == 9) {
            Long articleId = getById(id).getArticleId();
            String stringId = Long.toString(articleId);
            articleClient.deleteArticle(stringId);
        }

        wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery()
                .eq(WmNewsMaterial::getNewsId, id));
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * a list of article reviews
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authArticleList(NewsAuthDto dto) {
        dto.checkParam();
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!dto.getTitle().equals("")) {
            lambdaQueryWrapper.like(WmNews::getTitle, dto.getTitle());
        }

        if (dto.getStatus() == null) {
            lambdaQueryWrapper.ge(WmNews::getId, 1);
        } else {
            lambdaQueryWrapper.eq(WmNews::getStatus, dto.getStatus());
        }

        IPage page = new Page(dto.getPage(), dto.getSize());
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * details of the manual review of the article
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult articleDetails(Integer articleId) {
        if (articleId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmNews wmNews = getById(articleId);
        return ResponseResult.okResult(wmNews);
    }

    /**
     * manually approved
     * @param dto
     * @return
     */
    @Override
    public ResponseResult articleAuthPass(NewsAuthDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmNews wmNews = getById(dto.getId());
        wmNews.setStatus(dto.getStatus());
        boolean result = updateById(wmNews);
        if (result) {
            wmNewsAutoScanService.autoScanWmNews(dto.getId());
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SAVE_FAIL);
    }

    /**
     * manual review failed
     * @param dto
     * @return
     */
    @Override
    public ResponseResult articleAuthFail(NewsAuthDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmNews wmNews = getById(dto.getId());
        wmNews.setStatus(dto.getStatus());
        wmNews.setReason(dto.getMsg());
        boolean result = updateById(wmNews);
        return ResponseResult.okResult(result);
    }

    /**
     * update article
     * @param wemediaId
     * @return
     */
    @Override
    public ResponseResult getWemediaById(Integer wemediaId) {
        if (wemediaId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(getById(wemediaId));
    }

    /**
     * The first function: If the current cover type is automatic, set the data for the cover type
     * matching rules
     * 1. If the content image is greater than or equal to 1, it is less than 3 single image type 1
     * 2. If the content image is greater than or equal to 3, multi-image type 3
     * 3. If there is no picture in the content, there is no picture type 0
     *
     * The second function is to save the relationship between the cover image and the material
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();
//        If the current cover type is Automatic, set the data for the cover type
        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
//            multi image
            if (materials.size() >= 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            } else if (materials.size() >= 1 && materials.size() < 3) {
//            single image
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            } else {
//            no image
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }

//            revise the article
            if (images != null && images.size() > 0) {
                wmNews.setImages(StringUtils.join(images, ","));
            }
            updateById(wmNews);
        }
//        The second function is to save the relationship between the cover image and the material
        if (images != null && images.size() > 0) {
            saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
        }
    }

    /**
     * Handle the relationship between the content material and the image of the article
     * @param materials
     * @param newsId
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer newsId) {
        saveRelativeInfo(materials, newsId, WemediaConstants.WM_CONTENT_REFERENCE);
    }

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * The material relationship between the article and the image is stored in the database
     * @param materials
     * @param newsId
     * @param type
     */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if (materials != null && !materials.isEmpty()) {
//        query the id of the image by the url of the image
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery()
                    .in(WmMaterial::getUrl, materials));
//        determine if the material is valid
            if (dbMaterials == null || dbMaterials.size() == 0) {
//       Manually throw an exception The first function: it can prompt the caller that the material is invalid, and the second function is to roll back the data
                throw new CustomException(AppHttpCodeEnum.MATERIALS_REFERENCE_FAIL);
            }

            if (materials.size() != dbMaterials.size()) {
                throw new CustomException(AppHttpCodeEnum.MATERIALS_REFERENCE_FAIL);
            }
            List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
//        save in bulk
            wmNewsMaterialMapper.saveRelations(idList, newsId, type);
        }
    }

    /**
     * extract image information from the article
     * @param content
     * @return
     */
    private List<String> extractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if (map.get("type").equals("image")) {
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }
        return materials;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * save or modify the article
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
//        Complete attributes
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1); //listed by default

        if (wmNews.getId() == null) {
//            save
            save(wmNews);
        } else {
//            revise
//            Delete the relationship between the article image and the material
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery()
                    .eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            updateById(wmNews);
        }
    }
}
