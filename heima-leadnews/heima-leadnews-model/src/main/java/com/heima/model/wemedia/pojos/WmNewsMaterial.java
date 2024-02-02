package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * We media graphic reference material information table
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("wm_news_material")
public class WmNewsMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * material id
     */
    @TableField("material_id")
    private Integer materialId;

    /**
     * news id
     */
    @TableField("news_id")
    private Integer newsId;

    /**
     * Reference type
            0 content reference
            1 image reference
     */
    @TableField("type")
    private Short type;

    /**
     * Reference sort
     */
    @TableField("ord")
    private Short ord;

}