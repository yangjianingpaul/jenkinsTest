package com.heima.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app real name authentication information table
 * </p>
 *
 * @author yang
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
     * account id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * username
     */
    @TableField("name")
    private String name;

    /**
     * the name of the resource
     */
    @TableField("idno")
    private String idno;

    /**
     * front-photo
     */
    @TableField("font_image")
    private String fontImage;

    /**
     * back-photo
     */
    @TableField("back_image")
    private String backImage;

    /**
     * hand-held-photo
     */
    @TableField("hold_image")
    private String holdImage;

    /**
     * live-photographs
     */
    @TableField("live_image")
    private String liveImage;

    /**
     * state
            0 creating
            1 to be reviewed
            2 audit failed
            9 approved
     */
    @TableField("status")
    private Short status;

    /**
     * reason for rejection
     */
    @TableField("reason")
    private String reason;

    /**
     * creation time
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * submission time
     */
    @TableField("submited_time")
    private Date submitedTime;

    /**
     * updated
     */
    @TableField("updated_time")
    private Date updatedTime;

}