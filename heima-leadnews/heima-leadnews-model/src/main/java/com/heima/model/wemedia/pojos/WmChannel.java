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
 * channel info list
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("wm_channel")
public class WmChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * channel name
     */
    @TableField("name")
    private String name;

    /**
     * channel description
     */
    @TableField("description")
    private String description;

    /**
     * Default channel or not
     * 1：Default     true
     * 0：Non-default   false
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * Enable or not
     * 1：Enable   true
     * 0：Disable   false
     */
    @TableField("status")
    private Boolean status;

    /**
     * Default sort
     */
    @TableField("ord")
    private Integer ord;

    /**
     * Created time
     */
    @TableField("created_time")
    private Date createdTime;

}