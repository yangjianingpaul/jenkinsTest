package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;
import com.heima.common.baidu.GreenImageScan;
import com.heima.common.baidu.GreenTextScan;
import com.heima.common.tess4j.Tess4jClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;
    /**
     * We media article review
     * @param id    we-media article id
     */
    @Override
    @Async
    public void autoScanWmNews(Integer id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        1. Query we media article review
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            throw new RuntimeException("WmNewsAutoScanServiceImpl-Article does not exist");
        }

        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
//        Extract plain text content and images from content
            Map<String, Object> textAndImages = handleTextAndImages(wmNews);

//            Self-managing sensitive word filtering
            boolean isSensitive = handleSensitiveScan((String) textAndImages.get("content"), wmNews);
            if (!isSensitive) {
                return;
            }

//        2. Review text content Baidu Cloud interface
            String content = (String) textAndImages.get("content");
            if (StringUtils.isNotBlank(content)) {
                boolean isTextScan = handleTextScan(content, wmNews);
                if (!isTextScan) {
                    return;
                }
            }

//        3. Review picture Baidu cloud interface
            List<String> images = (List<String>) textAndImages.get("images");
            if (!images.equals(null) && images.size() > 0) {
                boolean isImageScan = handleImageScan(images, wmNews);
                if (!isImageScan) {
                    return;
                }
            }
        }

//        4。After the review is successful, the article data of relevant articles on the app side will be saved
        ResponseResult responseResult = saveAppArticle(wmNews);
        if (!responseResult.getCode().equals(200)) {
            throw new RuntimeException("WmNewsAutoScanServiceImpl-Failed to save related article data on the app. Procedure");
        }
//            Backfill the article id
        wmNews.setArticleId((Long) responseResult.getData());
        updateWmNews(wmNews, 9, "Successful audit");
    }

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;
    /**
     *
     * Self-managing sensitive word moderation
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleSensitiveScan(String content, WmNews wmNews) {
        boolean flag = true;
//        Get all the sensitive words
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
//        Initializes the sensitive word thesaurus
        SensitiveWordUtil.initMap(sensitiveList);
//        See if the article contains sensitive words
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if (map.size() > 0) {
            updateWmNews(wmNews, 2 ,"illegal content" + map);
            flag = false;
        }
        return flag;
    }

    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    /**
     * save the relevant article data on the app
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
//        copy property
        BeanUtils.copyProperties(wmNews, dto);
//        the layout of the article
        dto.setLayout(wmNews.getType());
//        channel
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if (wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }
//        author
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            dto.setAuthorName(wmUser.getName());
        }

//        set article id
        if (wmNews.getArticleId() != null) {
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());
        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;
    }

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private Tess4jClient tess4jClient;
    /**
     * Review picture
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = true;
        if (images == null || images.size() == 0) {
            return flag;
        }
//        Download images from minio
        images = images.stream().distinct().collect(Collectors.toList());
        List<byte[]> imageList = new ArrayList<>();

        try {
            for (String image : images) {
                byte[] bytes = fileStorageService.downLoadFile(image);
//        Tess4j:
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                BufferedImage bufferedImage = ImageIO.read(in);

//                Picture recognition
                String result = tess4jClient.doOCR(bufferedImage);
                if (!result.equals(null) && result.length() != 0) {
//                Filter text
                    boolean isSensitive = handleSensitiveScan(result, wmNews);
                    if (!isSensitive) {
                        return isSensitive;
                    }
                }
                imageList.add(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


//        Review picture
        try {
            for (byte[] bytes : imageList) {
                Map map = greenImageScan.imageScan(bytes);

                if (map != null) {
//                审核失败
                    if (map.get("conclusion").equals("不合规")) {
                        updateWmNews(wmNews, 2, "There is illegal content in the current article");
                        flag = false;
                        break;
                    }

//                Uncertain information, manual review required
                    if (map.get("conclusion").equals("审核失败") || map.get("conclusion").equals("疑似")) {
                        updateWmNews(wmNews, 3, "Uncertain information, manual review required");
                        flag = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    @Autowired
    private GreenTextScan greenTextScan;

    /**
     * Review plain text content
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) {

        boolean flag = true;

        if ((wmNews.getTitle() + "-" + content).length() == 1) {
            return flag;
        }

        try {
            Map map = greenTextScan.textScan(content);
            if (map != null) {
//                Audit failure
                if (map.get("conclusion").equals("不合规")) {
                    updateWmNews(wmNews, 2, "There is illegal content in the current article");
                    flag = false;
                }

//                Uncertain information, manual review required
                if (map.get("conclusion").equals("审核失败") || map.get("conclusion").equals("疑似")) {
                    updateWmNews(wmNews, 3, "Uncertain information, manual review required");
                    flag = false;
                }
            }
        } catch (Exception e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * Revise article content
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, int status, String reason) {
        wmNews.setStatus((short) status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 1。Extract text and pictures from the content of we-media articles
     * 2。Extract the cover image of the article
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> images = new ArrayList<>();
//        1。Extract text and pictures from the content of we-media articles
        if (StringUtils.isNotBlank(wmNews.getContent())) {
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("text")) {
                    stringBuilder.append(map.get("value"));
                }

                if (map.get("type").equals("image")) {
                    images.add((String) map.get("value"));
                }
            }
        }

//        2。Extract the cover image of the article
        if (StringUtils.isNotBlank(wmNews.getImages())) {
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content", stringBuilder.toString());
        resultMap.put("images", images);
        return resultMap;
    }
}
