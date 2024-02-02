package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * We media graphic content information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("wm_news")
public class WmNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * user id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * title
     */
    @TableField("title")
    private String title;

    /**
     * article content
     */
    @TableField("content")
    private String content;

    /**
     * article layout
            0 No picture article
            1 single image
            3 multi image
     */
    @TableField("type")
    private Short type;

    /**
     * channel id
     */
    @TableField("channel_id")
    private Integer channelId;

    @TableField("labels")
    private String labels;

    /**
     * created time
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * submit time
     */
    @TableField("submited_time")
    private Date submitedTime;

    /**
     * article status
            0 draft
            1 submit（To be reviewed）
            2 review failure
            3 manually review
            4 Manual approval
            8 approve（To be published）
            9 have published
     */
    @TableField("status")
    private Short status;

    /**
     * The release time is scheduled. If it is not scheduled, it is empty
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * reasons for refusal
     */
    @TableField("reason")
    private String reason;

    /**
     * article id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * The pictures are separated by commas
     */
    @TableField("images")
    private String images;

    @TableField("enable")
    private Short enable;
    
     //State enumeration class
    @Alias("WmNewsStatus")
    public enum Status{
        NORMAL((short)0),SUBMIT((short)1),FAIL((short)2),ADMIN_AUTH((short)3),ADMIN_SUCCESS((short)4),SUCCESS((short)8),PUBLISHED((short)9);
        short code;
        Status(short code){
            this.code = code;
        }
        public short getCode(){
            return this.code;
        }
    }

}