package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * We media user information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("wm_user")
public class WmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("ap_user_id")
    private Integer apUserId;

    @TableField("ap_author_id")
    private Integer apAuthorId;

    /**
     * Login user name
     */
    @TableField("name")
    private String name;

    /**
     * login password
     */
    @TableField("password")
    private String password;

    /**
     * salt
     */
    @TableField("salt")
    private String salt;

    /**
     * nickname
     */
    @TableField("nickname")
    private String nickname;

    /**
     * head portrait
     */
    @TableField("image")
    private String image;

    /**
     * location
     */
    @TableField("location")
    private String location;

    /**
     * phone number
     */
    @TableField("phone")
    private String phone;

    /**
     * status
            0 Temporarily unavailable
            1 Permanently unavailable
            9 Normally available
     */
    @TableField("status")
    private Integer status;

    /**
     * email
     */
    @TableField("email")
    private String email;

    /**
     * account type
            0 personal
            1 enterprise
            2 Subsidiary account
     */
    @TableField("type")
    private Integer type;

    /**
     * Operational score
     */
    @TableField("score")
    private Integer score;

    /**
     * latest login time
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * created time
     */
    @TableField("created_time")
    private Date createdTime;

}