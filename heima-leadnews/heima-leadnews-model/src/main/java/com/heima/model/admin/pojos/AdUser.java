package com.heima.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * administrator user information table
 * </p>
 *
 * @author yang
 */
@Data
@TableName("ad_user")
public class AdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    private Integer id;

    /**
     * login username
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
     * avatar
     */
    @TableField("image")
    private String image;

    /**
     * mobile phone number
     */
    @TableField("phone")
    private String phone;

    /**
     * state
            0 temporarily unavailable
            1 permanently disabled
            9 normal usability
     */
    @TableField("status")
    private Integer status;

    /**
     * mailbox
     */
    @TableField("email")
    private String email;

    /**
     * last login time
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * creation time
     */
    @TableField("created_time")
    private Date createdTime;

}