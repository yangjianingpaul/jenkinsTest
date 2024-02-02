package com.heima.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP real name authentication information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("ap_user_realname")
public class ApUserRealname implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Account ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * username
     */
    @TableField("name")
    private String name;

    /**
     * resource name
     */
    @TableField("idno")
    private String idno;

    /**
     * full face photo
     */
    @TableField("font_image")
    private String fontImage;

    /**
     * Back photo
     */
    @TableField("back_image")
    private String backImage;

    /**
     * Hand photo
     */
    @TableField("hold_image")
    private String holdImage;

    /**
     * Living photograph
     */
    @TableField("live_image")
    private String liveImage;

    /**
     * status
            0 Creating
            1 To be reviewed
            2 Review failure
            9 Approve
     */
    @TableField("status")
    private Short status;

    /**
     * The reason for refusal
     */
    @TableField("reason")
    private String reason;

    /**
     * Create time
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * Submit time
     */
    @TableField("submited_time")
    private Date submitedTime;

    /**
     * Update time
     */
    @TableField("updated_time")
    private Date updatedTime;

}