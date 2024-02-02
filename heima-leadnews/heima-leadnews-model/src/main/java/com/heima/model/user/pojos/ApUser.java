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
 * APP user information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("ap_user")
public class ApUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * major key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Encryption salt for passwords, communications, etc
     */
    @TableField("salt")
    private String salt;

    /**
     * username
     */
    @TableField("name")
    private String name;

    /**
     * Password, md5 encryption
     */
    @TableField("password")
    private String password;

    /**
     * cell-phone number
     */
    @TableField("phone")
    private String phone;

    /**
     * head portrait
     */
    @TableField("image")
    private String image;

    /**
     * 0 male
            1 female
            2 unknown
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 0 none
            1 true
     */
    @TableField("is_certification")
    private Boolean certification;

    /**
     * Identity authentication or not
     */
    @TableField("is_identity_authentication")
    private Boolean identityAuthentication;

    /**
     * 0 normal
            1 lock
     */
    @TableField("status")
    private Boolean status;

    /**
     * 0 normal user
            1 We-media people
            2 Internet celebrity
     */
    @TableField("flag")
    private Short flag;

    /**
     * registration time
     */
    @TableField("created_time")
    private Date createdTime;

}