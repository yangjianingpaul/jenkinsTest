package com.heima.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * An article information table that stores published articles
 * </p>
 *
 * @author itheima
 */

@Data
@TableName("ap_article")
public class ApArticle implements Serializable {

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;


    /**
     * title
     */
    private String title;

    /**
     * author id
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * Author's name
     */
    @TableField("author_name")
    private String authorName;

    /**
     * channel id
     */
    @TableField("channel_id")
    private Integer channelId;

    /**
     * The name of the channel
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * Article layout:0. No image article 1. Single image article 2. Multi-image article
     */
    private Short layout;

    /**
     * Article Tagged:0. Common Articles 1. Hot Articles 2. Pinned Articles 3. Featured Articles 4. web celebrity Articles
     */
    private Byte flag;

    /**
     * The cover image of the article, separated by multiple commas
     */
    private String images;

    /**
     * label
     */
    private String labels;

    /**
     * number of likes
     */
    private Integer likes;

    /**
     * number of favorites
     */
    private Integer collection;

    /**
     * number of reviews
     */
    private Integer comment;

    /**
     * number of reads
     */
    private Integer views;

    /**
     * provinces and cities
     */
    @TableField("province_id")
    private Integer provinceId;

    /**
     * urban
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * district
     */
    @TableField("county_id")
    private Integer countyId;

    /**
     * creation time
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * publish time
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * Synchronization status
     */
    @TableField("sync_status")
    private Boolean syncStatus;

    /**
     * source
     */
    private Boolean origin;

    /**
     * static page address
     */
    @TableField("static_url")
    private String staticUrl;
}
