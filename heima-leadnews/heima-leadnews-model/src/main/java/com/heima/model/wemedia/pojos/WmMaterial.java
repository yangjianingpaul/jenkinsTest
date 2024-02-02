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
 * We media graphic material information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("wm_material")
public class WmMaterial implements Serializable {

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
     * image url
     */
    @TableField("url")
    private String url;

    /**
     * Material type
            0 image
            1 video
     */
    @TableField("type")
    private Short type;

    /**
     * collection or not
     */
    @TableField("is_collection")
    private Short isCollection;

    /**
     * created time
     */
    @TableField("created_time")
    private Date createdTime;

}